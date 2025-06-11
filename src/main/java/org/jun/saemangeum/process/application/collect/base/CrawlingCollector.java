package org.jun.saemangeum.process.application.collect.base;

import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;

import java.util.List;

public abstract class CrawlingCollector implements Refiner {

    protected final TitleDuplicateChecker titleDuplicateChecker;

    public CrawlingCollector(TitleDuplicateChecker titleDuplicateChecker) {
        this.titleDuplicateChecker = titleDuplicateChecker;
    }

    @Override
    public List<Content> refine() {
        // 여기에 추상 메소드들 기반으로 로직 처리처리할 예정
        // 생성한 것들을 바탕으로 프로세스까지 처리하는 걸 여기에 책임을 부여하자(팩토리 메서드 취지)
        List<RefinedDataDTO> data = collectData();
        return data.stream().map(Content::create).toList();
    }

    // 각 페이지 크롤링과 관련된 추상메소드들
    public abstract List<RefinedDataDTO> collectData();
}
