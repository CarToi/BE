package org.jun.saemangeum.global.repository;

import org.jun.saemangeum.global.domain.Vector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VectorRepository extends JpaRepository<Vector, Long> {
}
