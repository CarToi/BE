package org.jun.saemangeum.service;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;
import org.jun.saemangeum.consume.domain.entity.Survey;
import org.jun.saemangeum.consume.domain.swap.ContentView;
import org.jun.saemangeum.consume.service.application.SurveyRecommendationService;
import org.jun.saemangeum.consume.service.domain.RecommendationLogService;
import org.jun.saemangeum.consume.service.domain.SurveyService;
import org.jun.saemangeum.consume.service.strategy.StrategyContextHolder;
import org.jun.saemangeum.consume.service.strategy.TableEmbeddingVectorStrategy;
import org.jun.saemangeum.consume.service.strategy.ViewEmbeddingVectorStrategy;
import org.jun.saemangeum.consume.util.CoordinateCalculator;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.Content;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
public class StrategyConcurrencyTest {

    @TestConfiguration
    static class MockEmbeddingVectorConfig {

        @Bean
        public TableEmbeddingVectorStrategy mockTableEmbeddingVectorStrategy() {
            Content content = Content.builder()
                    .title("Table Content")
                    .position("anywhere")
                    .category(Category.CULTURE)
                    .build();

            TableEmbeddingVectorStrategy mock = Mockito.mock(TableEmbeddingVectorStrategy.class);
            Mockito.when(mock.calculateSimilarity(Mockito.anyString()))
                    .thenReturn((List) List.of(content));

            return mock;
        }

        @Bean
        public ViewEmbeddingVectorStrategy mockViewEmbeddingVectorStrategy() {
            ContentView contentView = Mockito.mock(ContentView.class);
            Mockito.when(contentView.to()).thenReturn(
                    new RecommendationResponse(
                            "View Content",
                            "where",
                            Category.CULTURE,
                            "img",
                            "url",
                            null));

            ViewEmbeddingVectorStrategy mock = Mockito.mock(ViewEmbeddingVectorStrategy.class);
            Mockito.when(mock.calculateSimilarity(Mockito.anyString()))
                    .thenReturn((List) List.of(contentView));

            return mock;
        }

        @Bean(name = "mockCoordinateCalculator")
        public CoordinateCalculator mockCoordinateCalculator() {
            CoordinateCalculator mock = Mockito.mock(CoordinateCalculator.class);
            Mockito.when(mock.getCoordinate(Mockito.anyString())).thenReturn(null);

            return mock;
        }

        @Bean
        public SurveyRecommendationService mockSurveyRecommendationService() {
            SurveyService surveyService = Mockito.mock(SurveyService.class);
            RecommendationLogService logService = Mockito.mock(RecommendationLogService.class);

            Survey mockSurvey = Mockito.mock(Survey.class);
            Mockito.when(mockSurvey.getId()).thenReturn(1L); // NPE 방지
            Mockito.when(surveyService.save(Mockito.any())).thenReturn(mockSurvey);
            Mockito.doNothing().when(logService).saveALl(Mockito.anyList());

            return new SurveyRecommendationService(mockCoordinateCalculator(), surveyService, logService);
        }
    }

    @Autowired
    private SurveyRecommendationService mockSurveyRecommendationService;

    @Autowired
    private TableEmbeddingVectorStrategy mockTableEmbeddingVectorStrategy;

    @Autowired
    private ViewEmbeddingVectorStrategy mockViewEmbeddingVectorStrategy;

    @RepeatedTest(20)
    @DisplayName("스레드 비동기 전략 교체 중 동시 접근 → 전략 스레드 불안전성 발생 가능성 확인")
    void testStrategyThreadUnsafe() throws InterruptedException {
        // 초기 전략 세팅
        StrategyContextHolder.setStrategy(mockTableEmbeddingVectorStrategy);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        List<String> observedTitles = new CopyOnWriteArrayList<>();

        // 1. 파이프라인 전략 교체 스레드 (스케줄러의 별개 스레드풀 모식)
        executor.submit(() -> {
            try {
                Thread.sleep(50); // 소비 로직보다 늦게 실행되게 스레드 슬립 처리
                StrategyContextHolder.setStrategy(mockViewEmbeddingVectorStrategy);
                log.info("[데이터 수집 파이프라인] 전략 View로 변경");
            } catch (InterruptedException e) {
                log.error("스레드 인터럽트 발생");
            } finally {
                latch.countDown();
            }
        });

        // 2. 데이터 소비 호출 메인 스레드 (웹 요청 3티어 처리)
        executor.submit(() -> {
            try {
                Thread.sleep(100);
                var request = new SurveyCreateRequest(
                        "test",
                        28,
                        "남성",
                        "군산",
                        "군산",
                        "잔잔한",
                        "행복한");

                List<RecommendationResponse> responses =
                        mockSurveyRecommendationService.createRecommendationsBySurvey(request);
                observedTitles.add(responses.getFirst().getTitle());
                log.info("[데이터 소비 3티어] 사용된 추천 제목(전략): {}", responses.getFirst().getTitle());
            } catch (InterruptedException e) {
                log.error("스레드 인터럽트 발생");
            } finally
            {
                latch.countDown();
            }
        });

        latch.await();
        executor.shutdown();

        String usedTitle = observedTitles.getFirst();
        log.info("최종 추천(조회 전략): {}", usedTitle);

        // 1번 실행기에서 전략이 스왑 뷰로 바뀌면 그 전략을 2번 실행기에서 따라가야 한다.
        Assertions.assertEquals(
                "View Content",
                usedTitle,
                "전략 변경 반영 실패, 단순 전역 컨텍스트는 스레드 불안전");
    }
}
