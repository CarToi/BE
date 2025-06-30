package org.jun.saemangeum.consume.repository.swap;

import org.jun.saemangeum.consume.domain.swap.ContentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentViewRepository extends JpaRepository<ContentView, Long> {
}
