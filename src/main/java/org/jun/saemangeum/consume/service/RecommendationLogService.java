package org.jun.saemangeum.consume.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.entity.RecommendationLog;
import org.jun.saemangeum.consume.repository.RecommendationLogRepository;
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
}
