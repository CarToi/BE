package org.jun.saemangeum.pipeline.infrastructure.bean;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.jun.saemangeum.global.repository.CountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class Initializer {

    private final CountRepository countRepository;

    @PostConstruct
    @Transactional
    public void initializeCounts() {
        for (CollectSource source : CollectSource.values()) {
            boolean exists = countRepository.existsByCollectSource(source);
            if (!exists) {
                countRepository.save(Count.of(source, 0));
            }
        }
    }
}
