package org.jun.saemangeum.consume.repository.swap;

import java.util.List;
import org.jun.saemangeum.consume.domain.swap.ContentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentViewRepository extends JpaRepository<ContentView, Long> {
    @Query(value = """
        SELECT cv.*
        FROM recommendation_logs rl
        JOIN contents_view cv ON rl.content_title = cv.title
        WHERE rl.survey_client_id = :clientId
        """, nativeQuery = true)
    List<ContentView> findRecommendedContentViewsByClientId(@Param("clientId") String clientId);
}
