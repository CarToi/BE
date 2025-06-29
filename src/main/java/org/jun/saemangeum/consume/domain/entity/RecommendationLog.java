package org.jun.saemangeum.consume.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jun.saemangeum.global.domain.IContent;

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

    @Column(name = "content_title")
    private String contentTitle;

    @Column(name = "survey_id")
    private Long surveyId;

    public RecommendationLog(IContent content, Survey survey) {
        this.contentTitle = content.getTitle();
        this.surveyId = survey.getId();
    }
}
