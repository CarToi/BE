package org.jun.saemangeum.pipeline.application.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.consume.service.application.SurveyRecommendationService;
import org.jun.saemangeum.consume.service.strategy.TableEmbeddingVectorStrategy;
import org.jun.saemangeum.consume.service.strategy.ViewEmbeddingVectorStrategy;
import org.jun.saemangeum.consume.util.ViewJdbcUtil;
import org.jun.saemangeum.pipeline.application.service.PipelineService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PipelineScheduler {

    private final PipelineService pipelineService;
    private final ViewJdbcUtil viewJdbcUtil;
    private final SurveyRecommendationService surveyRecommendationService;
    private final TableEmbeddingVectorStrategy tableEmbeddingVectorStrategy;
    private final ViewEmbeddingVectorStrategy viewEmbeddingVectorStrategy;

    @Scheduled(cron = "0 0 3 ? * SUN")
    public void process() {
        log.info("[스케줄러] 스왑 뷰 추가");
        viewJdbcUtil.createViews();

        log.info("[스케줄러] DB 조회 무중단 처리, 임베딩 벡터 조회 스왑 뷰 전략 교체");
        surveyRecommendationService.setEmbeddingVectorStrategy(viewEmbeddingVectorStrategy);

        log.info("[스케줄러] 데이터 파이프라인 프로세스 시작");
        pipelineService.collectAndSaveAsync();
        log.info("[스케줄러] 데이터 파이프라인 프로세스 종료");

        log.info("[스케줄러] 임베딩 벡터 조회 전략 테이블 복귀 처리");
        surveyRecommendationService.setEmbeddingVectorStrategy(tableEmbeddingVectorStrategy);
    }
}
