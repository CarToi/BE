package org.jun.saemangeum.global.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.service.application.SurveyRecommendationService;
import org.jun.saemangeum.consume.service.application.TableEmbeddingVectorStrategy;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.jun.saemangeum.global.repository.CountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Initializer {

    private final CountRepository countRepository;
    private final TableEmbeddingVectorStrategy tableEmbeddingVectorStrategy;
    private final SurveyRecommendationService surveyRecommendationService;

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
        surveyRecommendationService.setEmbeddingVectorStrategy(tableEmbeddingVectorStrategy);
    }
}
