package org.jun.saemangeum.process.application.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
public abstract class OpenApiCollector implements Collector {
    private final WebClient webClient;

    @Override
    public List<RefinedDataDTO> collectData() {
        // 여기에 추상 메소드들 기반으로 로직 처리처리할 예정
        return List.of();
    }

    // 각 OpenAPI 호출과 관련된 추상메소드들
}
