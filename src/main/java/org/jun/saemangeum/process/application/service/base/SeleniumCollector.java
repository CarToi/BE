package org.jun.saemangeum.process.application.service.base;

import org.jun.saemangeum.global.persistence.domain.Content;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.openqa.selenium.WebDriver;

import java.util.List;

public abstract class SeleniumCollector implements Refiner {
    protected final WebDriver webDriver;

    public SeleniumCollector(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public List<Content> refine() {
        // 여기에 추상 메소드들 기반으로 로직 처리처리할 예정
        // 생성한 것들을 바탕으로 프로세스까지 처리하는 걸 여기에 책임을 부여하자(팩토리 메서드 취지)
        return List.of();
    }

    // 각 페이지 크롤링과 관련된 추상메소드들
    public abstract List<RefinedDataDTO> collectData();
}
