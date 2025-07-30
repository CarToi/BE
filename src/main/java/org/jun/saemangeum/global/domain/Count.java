package org.jun.saemangeum.global.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "data_counts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Count {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "collect_source")
    @Enumerated(EnumType.STRING)
    private CollectSource collectSource;

    @Column
    private int count;

    public static Count of(CollectSource collectSource, int count) {
        return Count.builder().collectSource(collectSource).count(count).build();
    }

    public void update(int count) {
        this.count = count;
    }
}
