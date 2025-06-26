package org.jun.saemangeum.pipeline.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.pipeline.application.collect.base.Refiner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentCollectService {

    private final List<Refiner> refiners;
    private final TaskExecutor virtualThreadExecutor;
    private final ContentService contentService;
    // ...+ 전처리용 서비스 로직 추가?

    @Transactional
    public void collectAndSave() {
        refiners.stream()
                .map(Refiner::refine)
                .map(refiner -> refiner.stream()
                        .peek(e -> simulateAiPreprocessing())
                        .toList())
                .forEach(contentService::saveContents);
    }

    @Transactional
    public void collectAndSaveByParallelStream() {
        refiners.parallelStream()
                .map(Refiner::refine)
                .map(refiner -> refiner.stream()
                        .peek(e -> simulateAiPreprocessing())
                        .toList())
                .forEach(contentService::saveContents);
    }

    // 실제 AI 연동 전처리 지연 시뮬레이션
    private void simulateAiPreprocessing() {
        try {
            Thread.sleep(100); // 항목당 100ms 지연 (AI 응답 시간 가정)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ************
    // 가상 스레드 기반
    // ************

    /**
     * Virtual Thread 기반 비동기 수집 - 메인 메서드
     */
    public CompletableFuture<Void> collectAndSaveAsync() {
        log.info("Virtual Thread 기반 데이터 수집 시작 - 총 {}개 수집기", refiners.size());

        // 모든 수집기를 독립적인 플로우로 실행
        List<CompletableFuture<Void>> futures = refiners.stream()
                .map(this::processRefinerFlow)
                .toList();

        // 모든 플로우 완료 대기
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("일부 수집기에서 오류 발생", throwable);
                    } else {
                        log.info("모든 데이터 수집 완료");
                    }
                });
    }

    /**
     * 개별 수집기의 전체 플로우 처리 (수집 → AI 전처리 → 저장)
     */
    public CompletableFuture<Void> processRefinerFlow(Refiner refiner) {
        return CompletableFuture.runAsync(() -> {
            String refinerName = refiner.getClass().getSimpleName();

            try {
                // 1단계: 데이터 수집
                List<Content> rawContents = refiner.refine();

                if (rawContents.isEmpty()) {
                    log.warn("수집된 데이터 없음: {}", refinerName);
                    return;
                }

                // 2단계: AI 전처리 (각 항목별로 처리)
                List<Content> processedContents = rawContents.stream()
                        .peek(content -> simulateAiPreprocessing())
                        .toList();

                // 3단계: 데이터베이스 저장
                contentService.saveContents(processedContents);

            } catch (Exception e) {
                log.error("플로우 실패: {} - 오류: {}", refinerName, e.getMessage(), e);
                // 개별 수집기 실패가 전체에 영향주지 않도록 예외를 던지지 않음
                // 필요시 재시도 로직이나 알림 로직 추가 가능
            }
        }, virtualThreadExecutor);
    }
}
