package org.jun.saemangeum.process.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.process.application.collect.base.Refiner;
import org.jun.saemangeum.process.application.embed.EmbeddingVectorService;
import org.jun.saemangeum.process.infrastructure.dto.EmbeddingJob;
import org.jun.saemangeum.process.infrastructure.queue.EmbeddingJobQueue;
import org.jun.saemangeum.process.infrastructure.queue.EmbeddingWorkerService;
import org.jun.saemangeum.process.presentation.TestDTO;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentDataProcessService {

    private final List<Refiner> refiners;
    private final TaskExecutor virtualThreadExecutor;
    private final ContentService contentService;
    private final EmbeddingWorkerService embeddingWorkerService;

    public void collectAndSaveAsync() {
        log.info("Virtual Thread 기반 데이터 수집 시작 - 총 {}개 수집기", refiners.size());

        // 모든 수집기를 독립적인 플로우로 실행
        List<CompletableFuture<Void>> futures = refiners.stream()
                .map(this::processRefinerFlow)
                .toList();

        // 모든 플로우 완료 대기
        // allOf 메소드가 리스트 타입을 파라미터로 안 받아서 배열로 바꿈
        // 빈 배열 생성 후 toArray 호출해서 리스트 크기에 맞춘 배열 생성
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("일부 수집기에서 오류 발생", throwable);
                    } else {
                        log.info("모든 데이터 수집 완료");
                        embeddingWorkerService.startWorker(); // 위치가 여기인가 밖인가...

                        Thread.ofVirtual().start(() -> {
                            try {
                                while (!embeddingWorkerService.isEmpty()) {
                                    Thread.sleep(300); // 큐 소비 기다리기
                                }
                                embeddingWorkerService.stopWorker();
                                log.info("임베딩 워커 종료 완료");

                                // 여기에 재시도 하드코딩?
                                Thread.ofVirtual().start(this::retryFailedJobs);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                log.warn("워커 종료 스레드 인터럽트 발생");
                            }
                        });
                    }
                });
    }

    /**
     * 개별 수집기의 전체 플로우 처리 (수집 → AI 전처리 → 저장)
     */
    private CompletableFuture<Void> processRefinerFlow(Refiner refiner) {
        return CompletableFuture.runAsync(() -> {
            String refinerName = refiner.getClass().getSimpleName();

            try {
                // 뭔가 Refiner 인터페이스에 데이터 개수 변화를 감지하는 추상 메소드도 넣어야 할 것 같은데..?
                // 어차피 자식은 추상이니 넘기고 손자 구현체들에서 구현하면 될 것 같은데...
                // 근데 기존의 구현체별 수집 데이터 개수를 어딘가에 저장해야 할 텐데.... 흠...
                // 데이터 수집 or 업데이트가 없다면 빈 리스트 반환
                List<Content> contents = refiner.refine();

                // 빈 리스트일 경우
                // 1) 데이터를 수집해봤으나 데이터가 없음
                // 2) 데이터의 업데이트가 없어서 빈 리스트가 반환됨
                if (contents.isEmpty()) {
                    log.warn("수집된 데이터 없음 or 업데이트 없음: {}", refinerName);
                    return;
                }

                // 솔직히.. 어떤 데이터가 추가됐는지 크롤링이나 API로는 알기 힘듦...
                // API는 어케어케 알 것 같은데 크롤링은 진짜 모르겠다(애시당초 어케 데이터 개수를 세리지..?)
                // Content에 수집 소스 분류 열거형도 추가해야 되나? 이를 통해 해당 내용 삭제(캐스케이드로 벡터도)
                // 그런 다음에 데이터베이스 저장(db에 먼저 저장하고 AI 전처리..?)
                contentService.saveContents(contents);
                contents.forEach(e -> embeddingWorkerService.offerEmbeddingJobQueue(new EmbeddingJob(e)));
            } catch (Exception e) {
                log.error("플로우 실패: {} - 오류: {}", refinerName, e.getMessage(), e);
                // 개별 수집기 실패가 전체에 영향주지 않도록 예외를 던지지 않음
                // 필요시 재시도 로직이나 알림 로직 추가 가능
            }
        }, virtualThreadExecutor);
    }

    /**
     * 실패 작업 재시도
     */
    private void retryFailedJobs() {
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
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("재시도 스레드 인터럽트 발생");
        }
    }
}
