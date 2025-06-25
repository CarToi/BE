package org.jun.saemangeum.consume.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.repository.RecommendationLogRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationLogService {

    private final RecommendationLogRepository recommendationLogRepository;

}
