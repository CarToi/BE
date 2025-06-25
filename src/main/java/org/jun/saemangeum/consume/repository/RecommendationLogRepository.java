package org.jun.saemangeum.consume.repository;

import org.jun.saemangeum.consume.domain.entity.RecommendationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationLogRepository extends JpaRepository<RecommendationLog, Long> {
}
