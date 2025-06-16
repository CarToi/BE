package org.jun.saemangeum.global.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "vectors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    private byte[] vector;

    @OneToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
