package org.jun.saemangeum.pipeline.application.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.consume.service.strategy.StrategyContextHolder;
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
    private final TableEmbeddingVectorStrategy tableEmbeddingVectorStrategy;
    private final ViewEmbeddingVectorStrategy viewEmbeddingVectorStrategy;

    @Scheduled(cron = "0 0 3 ? * SUN") // 얘 자체가 애시당초 다른 스레드 풀에서 돌아가게 처리
    public void process() {
        try {
            log.info("{} - 파이프라인 프로세스 시작", Thread.currentThread().getName());
            log.info("[스케줄러] 스왑 뷰 추가");
            viewJdbcUtil.createViews();

            log.info("[스케줄러] DB 조회 무중단 처리, 임베딩 벡터 조회 스왑 뷰 전략 교체");
            StrategyContextHolder.setStrategy(viewEmbeddingVectorStrategy);

            log.info("[스케줄러] 데이터 파이프라인 프로세스 시작");
            pipelineService.flowPipeline().join();
            log.info("[스케줄러] 데이터 파이프라인 프로세스 종료");
        } catch (Exception e) {
            log.error("[스케줄러] 파이프라인 중 {} 예외 발생", e.getClass().getSimpleName(), e);
        } finally {
            log.info("[스케줄러] 임베딩 벡터 조회 전략 테이블 복귀 처리");
            StrategyContextHolder.setStrategy(tableEmbeddingVectorStrategy);

            log.info("[스케줄러] 스왑 뷰 삭제");
            viewJdbcUtil.dropViews();
        }
    }
}
