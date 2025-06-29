package org.jun.saemangeum.global.repository;

import org.jun.saemangeum.global.domain.Vector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VectorRepository extends JpaRepository<Vector, Long> {
    @Query("SELECT v FROM Vector v JOIN FETCH v.content")
    List<Vector> findAllWithContent();
}
