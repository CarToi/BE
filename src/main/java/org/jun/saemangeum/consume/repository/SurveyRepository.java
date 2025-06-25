package org.jun.saemangeum.consume.repository;

import org.jun.saemangeum.consume.domain.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
