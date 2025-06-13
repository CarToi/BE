package org.jun.saemangeum.global.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "vectors")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Vector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    private String vector;

    @OneToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
