package org.jun.saemangeum.process.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.process.application.collect.base.Refiner;
import org.jun.saemangeum.process.application.embed.EmbeddingVectorService;
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
    private final EmbeddingVectorService embeddingVectorService;

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

                // 1단계: 데이터 수집
                List<Content> contents = refiner.refine();

                if (contents.isEmpty()) {
                    log.warn("수집된 데이터 없음: {}", refinerName);
                    return;
                }

                // 3단계: 데이터베이스 저장(db에 먼저 저장하고 AI 전처리..?)
                contentService.saveContents(contents);


                // 이 사이에 블로킹 큐가 필요할 것 같다
                embeddingVectorService.embeddingVector(contents);
            } catch (Exception e) {
                log.error("플로우 실패: {} - 오류: {}", refinerName, e.getMessage(), e);
                // 개별 수집기 실패가 전체에 영향주지 않도록 예외를 던지지 않음
                // 필요시 재시도 로직이나 알림 로직 추가 가능
            }
        }, virtualThreadExecutor);
    }

    public List<TestDTO> suggestContent(String text) {
        List<Content> contents = embeddingVectorService.calculateSimilarity(text);
        return contents.stream().map(TestDTO::of).toList();
    }
}
