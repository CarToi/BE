package org.jun.saemangeum.consume.repository.swap;

import org.jun.saemangeum.consume.domain.swap.VectorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VectorViewRepository extends JpaRepository<VectorView, Long> {
    @Query("SELECT v FROM VectorView v JOIN FETCH v.contentView")
    List<VectorView> findAllWithContentView();
}
