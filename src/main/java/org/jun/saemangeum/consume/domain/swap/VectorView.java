package org.jun.saemangeum.consume.domain.swap;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(
        name = "vectors_view",
        indexes = @Index(name = "fk_vector_view", columnList = "content_id") // 외래키 제약조건 제거로 직접 인덱스 세팅
)
public class VectorView {
    @Id
    private Long id;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] vector;

    @OneToOne
    @JoinColumn(
            name = "content_id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT) // 외래키 제약조건 제거
    )
    private ContentView contentView;
}
