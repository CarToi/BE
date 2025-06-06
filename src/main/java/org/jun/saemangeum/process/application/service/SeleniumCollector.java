package org.jun.saemangeum.process.application.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.openqa.selenium.WebDriver;

import java.util.List;

@RequiredArgsConstructor
public abstract class SeleniumCollector implements Collector {
    private final WebDriver webDriver;

    @Override
    public List<RefinedDataDTO> collectData() {
        // 여기에 추상 메소드들 기반으로 로직 처리처리할 예정
        return List.of();
    }

    // 각 페이지 크롤링과 관련된 추상메소드들
}
