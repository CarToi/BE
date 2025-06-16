package org.jun.saemangeum.global.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
import org.jun.saemangeum.process.application.util.CollectSource;

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
    @Column
    private String introduction;

    @Column
    private CollectSource collectSource;

    @OneToOne(mappedBy = "content", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "vector_id")
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
}
