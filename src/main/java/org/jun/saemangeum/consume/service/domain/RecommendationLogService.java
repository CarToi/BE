package org.jun.saemangeum.consume.service.domain;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.AverageRequest;
import org.jun.saemangeum.consume.domain.entity.RecommendationLog;
import org.jun.saemangeum.consume.repository.entity.RecommendationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationLogService {

    private final RecommendationLogRepository recommendationLogRepository;

    public void saveALl(List<RecommendationLog> recommendationLogs) {
        recommendationLogRepository.saveAll(recommendationLogs);
    }

    // 너도 캐시 적용이 될까?
    public List<Long> getRecommendationLogIdsJoinSurveys(AverageRequest request) {
        return recommendationLogRepository.findContentIdsBySurveyConditions(
                request.age(), request.gender(), request.city(), request.want(), request.mood());
    }
}
