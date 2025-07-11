package org.jun.saemangeum.global.repository;

import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.CollectSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByCollectSource(CollectSource collectSource);
    void deleteByCollectSource(CollectSource collectSource);
    int countByCollectSource(CollectSource collectSource);

    @Query(value = """
        SELECT c.*
        FROM recommendation_logs rl
        JOIN contents c ON rl.content_title = c.title
        WHERE rl.survey_client_id = :clientId
        """, nativeQuery = true)
    List<Content> findRecommendedContentsByClientId(@Param("clientId") String clientId);
} 