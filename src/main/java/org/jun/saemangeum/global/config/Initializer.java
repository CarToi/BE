package org.jun.saemangeum.global.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.consume.service.strategy.StrategyContextHolder;
import org.jun.saemangeum.consume.service.strategy.TableEmbeddingVectorStrategy;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.jun.saemangeum.global.repository.CountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer {

    private final CountRepository countRepository;
    private final TableEmbeddingVectorStrategy tableEmbeddingVectorStrategy;

    @PostConstruct
    @Transactional
    public void initializeCounts() {
        for (CollectSource source : CollectSource.values()) {
            boolean exists = countRepository.existsByCollectSource(source);
            if (!exists) {
                countRepository.save(Count.of(source, 0));
            }
        }
    }

    @PostConstruct
    public void setEmbeddingStrategy() {
        // 전략 초기화
        log.info("테이블 기반 임베딩 벡터 조회 전략 처리");
        StrategyContextHolder.setStrategy(tableEmbeddingVectorStrategy);
//        surveyRecommendationService.setEmbeddingVectorStrategy(tableEmbeddingVectorStrategy);
    }
}
