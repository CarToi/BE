package org.jun.saemangeum.consume.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jun.saemangeum.global.domain.Content;

@Entity
@Builder
@Table(name = "recommendation_logs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "survey_id")
    private Long surveyId;

    public RecommendationLog(Content content, Survey survey) {
        this.contentId = content.getId();
        this.surveyId = survey.getId();
    }
}
