package org.jun.saemangeum.global.repository;

import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
} 