package org.jun.saemangeum.consume.domain.swap;

import jakarta.persistence.*;
import lombok.Getter;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.IContent;

@Getter
@Entity
@Table(name = "contents_view")
public class ContentView implements IContent {
    @Id
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

    @OneToOne(mappedBy = "contentView")
    private VectorView vectorView;

    @Override
    public RecommendationResponse to() {
        return new RecommendationResponse(title, position, category, image, url, null);
    }

    @Override
    public String getTitle() {
        return title;
    }
}
