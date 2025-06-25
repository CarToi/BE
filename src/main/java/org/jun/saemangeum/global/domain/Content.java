package org.jun.saemangeum.global.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;

@Entity
@Builder
@Table(name = "contents")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column
    private String image;

    @Column
    private String url;

    @Lob // MySQL 등에서는 TEXT 등으로
    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column
    private CollectSource collectSource;

    @OneToOne(mappedBy = "content", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    @JoinColumn(name = "vector_id") // 연관관계 주인이 벡터인데 굳이 얜 필요없겠네
    private Vector vector;

    public static Content create(RefinedDataDTO dto) {
        return Content.builder()
                .title(dto.title())
                .position(dto.position())
                .category(dto.category())
                .image(dto.image())
                .introduction(dto.introduction())
                .url(dto.url())
                .collectSource(dto.collectSource())
                .build();
    }

    public void setVector(Vector vector) {
        this.vector = vector;
        if (vector.getContent() != null) {
            vector.setContent(this);
        }
    }

    public RecommendationResponse to() {
        return new RecommendationResponse(title, position, category, image, url);
    }
}
