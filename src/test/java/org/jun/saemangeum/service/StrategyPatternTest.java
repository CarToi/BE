package org.jun.saemangeum.service;

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
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.Content;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootTest
public class StrategyPatternTest {

    @TestConfiguration
    static class MockEmbeddingVectorConfig {
        @Bean(name = "mockTableEmbeddingVectorStrategy")
        public TableEmbeddingVectorStrategy mockTableEmbeddingVectorStrategy() {
            // title : String
            Content content = Content.builder()
                    .title("Table Content")
                    .position("where")
                    .category(Category.CULTURE)
                    .build();

            TableEmbeddingVectorStrategy mock = Mockito.mock(TableEmbeddingVectorStrategy.class);
            Mockito.when(mock.calculateSimilarity(Mockito.anyString())).thenReturn((List) List.of(content));

            return mock;
        }

        @Bean(name = "mockViewEmbeddingVectorStrategy")
        public ViewEmbeddingVectorStrategy mockViewEmbeddingVectorStrategy() {
            // title : String
            ContentView mockContentView = Mockito.mock(ContentView.class);
            RecommendationResponse dummyResponse =
                    new RecommendationResponse("View Content", "position", Category.CULTURE, "image", "url");
            Mockito.when(mockContentView.to()).thenReturn(dummyResponse);

            ViewEmbeddingVectorStrategy mock = Mockito.mock(ViewEmbeddingVectorStrategy.class);
            Mockito.when(mock.calculateSimilarity(Mockito.anyString())).thenReturn((List) List.of(mockContentView));

            return mock;
        }

        @Bean(name = "mockSurveyRecommendationService")
        public SurveyRecommendationService mockSurveyRecommendationService() {
            SurveyService mockSurveyService = Mockito.mock(SurveyService.class);
            RecommendationLogService mockRecommendationLogService = Mockito.mock(RecommendationLogService.class);

            // Survey 응답을 미리 만들어줌
            Survey mockSurvey = Mockito.mock(Survey.class);
            Mockito.when(mockSurvey.getId()).thenReturn(1L); // NPE 방지용
            Mockito.when(mockSurveyService.save(Mockito.any())).thenReturn(mockSurvey);

            Mockito.doNothing().when(mockRecommendationLogService).saveALl(Mockito.anyList());

            return new SurveyRecommendationService(mockSurveyService, mockRecommendationLogService);
        }
    }

    @Autowired
    private SurveyRecommendationService mockSurveyRecommendationService;

    @Autowired
    private TableEmbeddingVectorStrategy mockTableEmbeddingVectorStrategy;

    @Autowired
    private ViewEmbeddingVectorStrategy mockViewEmbeddingVectorStrategy;

    @Test
    @DisplayName("전략별 반환 타입 확인 테스트")
    void testStrategyPattern() {
        SurveyCreateRequest request = new SurveyCreateRequest(
                "clientId",
                10,
                "gender",
                "resident",
                "city",
                "mood",
                "want");

        // 테이블 조회 전략 세팅
        StrategyContextHolder.setStrategy(mockTableEmbeddingVectorStrategy);
        List<RecommendationResponse> result = mockSurveyRecommendationService.createRecommendationsBySurvey(request);
        RecommendationResponse first = result.getFirst();
        Assertions.assertEquals(first.title().getClass(), String.class);
        Assertions.assertEquals(first.title(), "Table Content");

        // 스왑 뷰 조회 전략 세팅
        StrategyContextHolder.setStrategy(mockViewEmbeddingVectorStrategy);
        result = mockSurveyRecommendationService.createRecommendationsBySurvey(request);
        first = result.getFirst();
        Assertions.assertEquals(first.title().getClass(), String.class);
        Assertions.assertEquals(first.title(), "View Content");
    }
}
