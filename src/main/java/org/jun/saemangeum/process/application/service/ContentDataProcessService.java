package org.jun.saemangeum.process.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.process.application.collect.base.Refiner;
import org.jun.saemangeum.process.application.monitor.alarm.*;
import org.jun.saemangeum.process.infrastructure.dto.EmbeddingJob;
import org.jun.saemangeum.process.infrastructure.queue.EmbeddingWorkerService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class ContentDataProcessService {

    private final List<Refiner> refiners;
    private final TaskExecutor virtualThreadExecutor;
    private final ContentService contentService;
    private final EmbeddingWorkerService embeddingWorkerService;
    private final List<Alarm> alarms;
    private final AtomicBoolean nonUpdate;

    public ContentDataProcessService(
            List<Refiner> refiners,
            TaskExecutor virtualThreadExecutor,
            ContentService contentService,
            EmbeddingWorkerService embeddingWorkerService,
            List<Alarm> alarms) {
        this.refiners = refiners;
        this.virtualThreadExecutor = virtualThreadExecutor;
        this.contentService = contentService;
        this.embeddingWorkerService = embeddingWorkerService;
        this.alarms = alarms;
        this.nonUpdate = new AtomicBoolean(true);
    }

    /**
     * 전체 플로우 (수집 → AI 전처리(+ 재시도) → 저장)
     */
    public void collectAndSaveAsync() {
        log.info("Virtual Thread 기반 데이터 수집 시작 - 총 {}개 수집기", refiners.size());

        // 모든 수집기를 독립적인 플로우로 실행
        List<CompletableFuture<Void>> futures = refiners.stream()
                .map(this::processCollectingAndRefiningFlow)
                .toList();

        // 모든 플로우 완료 대기
        // allOf 메소드가 리스트 타입을 파라미터로 안 받아서 배열로 바꿈
        // 빈 배열 생성 후 toArray 호출해서 리스트 크기에 맞춘 배열 생성
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("일부 수집기에서 오류 발생", throwable);
                    } else {
                        boolean flag = nonUpdate.get();
                        log.info("모든 데이터 수집 완료, 업데이트 여부: {}", flag);

                        if (flag) {
                            alarms.forEach(alarm -> alarm.sendAlarm(
                                    () -> AlarmPayload.builder()
                                            .process(AlarmProcess.COLLECT)
                                            .alarmType(AlarmType.SUCCESS)
                                            .alarmMessage(AlarmMessage.UNCHANGED)
                                            .threadName("Data Collect Flow")
                                            .timestamp(Instant.now())
                                            .build()));
                        }

                        processEmbeddingVectorFlow();
                    }
                });
    }

    /**
     * 개별 수집기의 전체 플로우 처리
     */
    private CompletableFuture<Void> processCollectingAndRefiningFlow(Refiner refiner) {
        return CompletableFuture.runAsync(() -> {
            String refinerName = refiner.getClass().getSimpleName();

            try {
                List<Content> contents = refiner.refine();

                // 업데이트 x
                if (contents.isEmpty()) {
                    log.warn("수집된 데이터 없음 or 업데이트 없음: {}", refinerName);
                    return;
                }

                // 업데이트 o
                // 한 번만 false로 바뀌도록 최적화
                nonUpdate.compareAndSet(true, false);

                contentService.saveContents(contents);
                alarms.forEach(alarm -> alarm.sendAlarm(
                        () -> AlarmPayload.builder()
                                .process(AlarmProcess.COLLECT)
                                .alarmType(AlarmType.SUCCESS)
                                .alarmMessage(AlarmMessage.COLLECT)
                                .threadName(refinerName)
                                .timestamp(Instant.now())
                                .build(), contents.size()));
                contents.forEach(e -> embeddingWorkerService.offerEmbeddingJobQueue(new EmbeddingJob(e)));
            } catch (Exception e) {
                log.error("플로우 실패: {} - 오류: {}", refinerName, e.getMessage(), e);
                alarms.forEach(alarm -> alarm.sendAlarm(
                        () -> AlarmPayload.builder()
                                .process(AlarmProcess.COLLECT)
                                .alarmType(AlarmType.ERROR)
                                .alarmMessage(AlarmMessage.ERROR)
                                .threadName(refinerName)
                                .timestamp(Instant.now())
                                .build(), e.getClass().getSimpleName()));
            }
        }, virtualThreadExecutor);
    }

    /**
     * 임베딩 벡터 워커플로우 진입
     */
    private void processEmbeddingVectorFlow() {
        embeddingWorkerService.startWorker();

        Thread.ofVirtual().start(() -> {
            try {
                if (embeddingWorkerService.isEmpty()) {
                    log.info("임베딩 벡터 대상 데이터 없음");
                    alarms.forEach(alarm -> alarm.sendAlarm(
                            () -> AlarmPayload.builder()
                                    .process(AlarmProcess.EMBEDDING)
                                    .alarmType(AlarmType.SUCCESS)
                                    .alarmMessage(AlarmMessage.UNCHANGED)
                                    .threadName("Embedding Vector Flow")
                                    .timestamp(Instant.now())
                                    .build()));
                    return;
                }

                while (!embeddingWorkerService.isEmpty()) {
                    Thread.sleep(300);
                }
                embeddingWorkerService.stopWorker();
                log.info("임베딩 워커 플로우 종료");
                alarms.forEach(alarm -> alarm.sendAlarm(
                        () -> AlarmPayload.builder()
                                .process(AlarmProcess.EMBEDDING)
                                .alarmType(AlarmType.SUCCESS)
                                .alarmMessage(AlarmMessage.EMBEDDING)
                                .threadName("Embedding Vector Flow")
                                .timestamp(Instant.now())
                                .build(),
                        embeddingWorkerService.getFailedContents().size()));

                Thread.ofVirtual().start(this::retryFailedEmbeddingVectorJobs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("워커 종료 스레드 인터럽트 발생");
                alarms.forEach(alarm -> alarm.sendAlarm(
                        () -> AlarmPayload.builder()
                                .process(AlarmProcess.EMBEDDING)
                                .alarmType(AlarmType.ERROR)
                                .alarmMessage(AlarmMessage.ERROR)
                                .threadName("Embedding Vector Flow")
                                .timestamp(Instant.now())
                                .build(), e.getClass().getSimpleName()));
            }
        });
    }

    /**
     * 임베딩 벡터 실패 작업 재시도
     */
    private void retryFailedEmbeddingVectorJobs() {
        try {
            List<Content> failed = embeddingWorkerService.getFailedContents();
            if (failed.isEmpty()) {
                log.info("재시도할 콘텐츠 없음");
                return;
            }

            log.info("재시도 대기 중 (10초)");
            Thread.sleep(15_000); // 유예 시간 15초

            log.info("재시도 시작 - {}개 실패 콘텐츠", failed.size());
            failed.forEach(content ->
                    embeddingWorkerService.offerEmbeddingJobQueue(new EmbeddingJob(content)));

            embeddingWorkerService.startWorker();

            while (!embeddingWorkerService.isEmpty()) {
                Thread.sleep(300);
            }

            embeddingWorkerService.stopWorker();
            log.info("재시도 워커 종료 완료");
            alarms.forEach(alarm -> alarm.sendAlarm(
                    () -> AlarmPayload.builder()
                            .process(AlarmProcess.RETRY)
                            .alarmType(AlarmType.SUCCESS)
                            .alarmMessage(AlarmMessage.RETRY)
                            .threadName("Retry Embedding Flow")
                            .timestamp(Instant.now())
                            .build()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("재시도 스레드 인터럽트 발생");
            alarms.forEach(alarm -> alarm.sendAlarm(
                    () -> AlarmPayload.builder()
                            .process(AlarmProcess.RETRY)
                            .alarmType(AlarmType.ERROR)
                            .alarmMessage(AlarmMessage.ERROR)
                            .threadName("Retry Embedding Flow")
                            .timestamp(Instant.now())
                            .build(), e.getClass().getSimpleName()));
        }
    }
}
