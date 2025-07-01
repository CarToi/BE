package org.jun.saemangeum.pipeline.application.service;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.pipeline.application.collect.base.Refiner;
import org.jun.saemangeum.pipeline.domain.service.AlarmService;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingJob;
import org.jun.saemangeum.pipeline.infrastructure.queue.EmbeddingJobQueue;
import org.jun.saemangeum.pipeline.infrastructure.queue.EmbeddingWorker;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class PipelineService {

    private final List<Refiner> refiners;
    private final TaskExecutor virtualThreadExecutor;
    private final ContentService contentService;
    private final AlarmService alarmService;
    private final AtomicBoolean nonUpdate;
    private final EmbeddingVectorService embeddingVectorService;

    private List<EmbeddingJob> failedEmbeddingJobList;
    private EmbeddingJobQueue embeddingJobQueue;

    public PipelineService(
            List<Refiner> refiners,
            TaskExecutor virtualThreadExecutor,
            ContentService contentService,
            AlarmService alarmService, EmbeddingVectorService embeddingVectorService) {
        this.refiners = refiners;
        this.virtualThreadExecutor = virtualThreadExecutor;
        this.contentService = contentService;
        this.alarmService = alarmService;
        this.nonUpdate = new AtomicBoolean(true);
        this.embeddingJobQueue = new EmbeddingJobQueue();
        this.embeddingVectorService = embeddingVectorService;
    }

    /**
     * 전체 플로우 (수집 → AI 전처리(+ 재시도) → 저장)
     */
    public CompletableFuture<Void> flowPipeline() {
        log.info("Virtual Thread 기반 데이터 수집 시작 - 총 {}개 수집기", refiners.size());

        // 모든 수집기 독립적인 플로우로 실행
        // allOf 메소드가 리스트 타입을 파라미터로 안 받아서 배열로 바꿈
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                refiners.stream()
                        .map(this::processCollectingAndRefiningFlow)
                        .toArray(CompletableFuture[]::new)
        );

        return allTasks.thenRun(() -> {
            boolean flag = nonUpdate.get();
            log.info("모든 데이터 수집 완료, 업데이트 안됐는지 여부: {}", flag);

            if (flag) alarmService.sendUnchanged("Data Collect Flow");

            processEmbeddingVectorFlow();
            processRetryFailedEmbeddingJobs();
        }).exceptionally(throwable -> {
            log.error("일부 수집기에서 오류 발생", throwable);
            return null;
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

                // 업데이트 o, 한 번만 false로 바뀌도록 최적화
                nonUpdate.compareAndSet(true, false);
                contentService.saveContents(contents);
                alarmService.sendCollectSuccess(refinerName, contents.size());
                contents.forEach(e -> embeddingJobQueue.offerQueue(new EmbeddingJob(e)));
            } catch (Exception e) {
                log.error("플로우 실패: {} - 오류: {}", refinerName, e.getMessage(), e);
                alarmService.sendCollectError(refinerName, e);
            }
        }, virtualThreadExecutor);
    }

    /**
     * 임베딩 벡터 워커플로우 진입
     */
    private void processEmbeddingVectorFlow() {
        // 임베딩 벡터 파이프 리팩토링
        if (embeddingJobQueue.isEmptyQueue()) {
            log.info("임베딩 벡터 대상 데이터 없음");
            alarmService.sendEmbeddingUnchanged("Embedding Vector Flow");
            return;
        }

        EmbeddingWorker worker = new EmbeddingWorker(embeddingJobQueue, embeddingVectorService);
        log.info("임베딩 벡터 워커 실행 시작");
        Thread.ofVirtual().start(worker);

        // 큐가 빌 때까지 대기
        while (!embeddingJobQueue.isEmptyQueue()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("임베딩 워커 진행 중 인터럽트 발생");
                alarmService.sendEmbeddingError("Embedding Vector Flow", e.getClass().getSimpleName());
                return;
            }
        }

        // 실패 리스트 확인
        failedEmbeddingJobList = worker.getFailedContents();
        if (failedEmbeddingJobList.isEmpty()) {
            log.info("임베딩 벡터 워커 성공적으로 완료");
            alarmService.sendEmbeddingSuccess("Embedding Vector Flow", 0);
        } else {
            log.warn("임베딩 벡터 워커 일부 실패 - {}건", failedEmbeddingJobList.size());
            alarmService.sendEmbeddingSuccess("Embedding Vector Flow", failedEmbeddingJobList.size());
        }
    }

    /**
     * 임베딩 벡터 실패 작업 재시도
     */
    private void processRetryFailedEmbeddingJobs() {
        // 임베딩 벡터 재시도 파이프 리팩토링
        // 전 작업에서 큐가 다 비워진 걸 실패 리스트로 다시 채운다
        if (failedEmbeddingJobList == null || failedEmbeddingJobList.isEmpty()) {
            log.info("재시도할 콘텐츠 없음");
            return;
        }

        try {
            log.info("재시도 대기 중 (15초)");
            Thread.sleep(15_000); // 유예 시간 15초
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("재시도 스레드 인터럽트 발생");
            alarmService.sendRetryError("Retry Embedding Flow", e.getClass().getSimpleName());
        }

        embeddingJobQueue.updateFailedList(failedEmbeddingJobList);
        EmbeddingWorker worker = new EmbeddingWorker(embeddingJobQueue, embeddingVectorService);

        log.info("재시도 시작 - {}개 실패 콘텐츠", failedEmbeddingJobList.size());
        Thread.ofVirtual().start(worker);

        // 큐가 빌 때까지 대기
        while (!embeddingJobQueue.isEmptyQueue()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("재시도 워커 진행 중 인터럽트 발생");
                alarmService.sendRetryError("Embedding Vector Flow", e.getClass().getSimpleName());
                return;
            }
        }

        log.info("재시도 워커 종료 완료");

        if (worker.getFailedContents().isEmpty()) {
            alarmService.sendRetrySuccess("Retry Embedding Flow");
        } else {
            alarmService.sendRetryError("Retry Embedding Flow",
                    worker.getFailedContents().size() + " 개 데이터 최종 임베딩 벡터 처리 실패");
        }

        embeddingJobQueue = new EmbeddingJobQueue();
        failedEmbeddingJobList.clear();
    }
}
