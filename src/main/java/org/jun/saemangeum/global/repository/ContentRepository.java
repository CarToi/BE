package org.jun.saemangeum.global.repository;

import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.CollectSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByCollectSource(CollectSource collectSource);
    void deleteByCollectSource(CollectSource collectSource);
    int countByCollectSource(CollectSource collectSource);
} 