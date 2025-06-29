package org.jun.saemangeum.consume.domain.swap;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "vectors_view")
public class VectorView {
    @Id
    private Long id;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] vector;

    @OneToOne
    @JoinColumn(name = "content_id")
    private ContentView contentView;
}
