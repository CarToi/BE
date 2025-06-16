package org.jun.saemangeum.global.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.jun.saemangeum.global.repository.CountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountService {

    private final CountRepository countRepository;

    @Transactional(readOnly = true)
    public Count findByCollectSource(CollectSource collectSource) {
        return countRepository
                .findByCollectSource(collectSource)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 데이터 소스 식별 코드!"));
    }
}
