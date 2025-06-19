package org.jun.saemangeum.global.repository;

import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountRepository extends JpaRepository<Count, Long> {
    Optional<Count> findByCollectSource(CollectSource collectSource);
    boolean existsByCollectSource(CollectSource collectSource);
}
