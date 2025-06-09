package org.jun.saemangeum.global.persistence.repository;

import org.jun.saemangeum.global.persistence.domain.Category;
import org.jun.saemangeum.global.persistence.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByCategory(Category category);
} 