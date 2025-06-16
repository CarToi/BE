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

    // 트랜잭션 read only 해버리면
    // 이 트랜잭션 내부에서 읽기만 할 거니까, 쓰기 관련 영속성 관리도 하지 말고, 성능 최적화 해줘 하는 셈
    @Transactional
    public Count findByCollectSource(CollectSource collectSource) {
        return countRepository
                .findByCollectSource(collectSource)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 데이터 소스 식별 코드!"));
    }
}
