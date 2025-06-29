package org.jun.saemangeum.consume.repository.swap;

import org.jun.saemangeum.consume.domain.swap.VectorView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VectorViewRepository extends JpaRepository<VectorView, Long> {
}
