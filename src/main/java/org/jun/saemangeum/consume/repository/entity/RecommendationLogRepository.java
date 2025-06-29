package org.jun.saemangeum.consume.repository.entity;

import org.jun.saemangeum.consume.domain.entity.RecommendationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationLogRepository extends JpaRepository<RecommendationLog, Long> {
    @Query(value = """
        SELECT rl.content_id
        FROM recommendation_logs rl
        JOIN surveys s ON rl.survey_id = s.id
        WHERE s.age = :age
          AND s.gender = :gender
          AND s.city = :city
          AND s.want = :want
          AND s.mood = :mood
        """, nativeQuery = true)
    List<Long> findContentIdsBySurveyConditions(
            @Param("age") int age,
            @Param("gender") String gender,
            @Param("city") String city,
            @Param("want") String want,
            @Param("mood") String mood
    );
}
