package org.jun.saemangeum.process.application.collect.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.jun.saemangeum.process.infrastructure.api.OpenApiClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class OpenApiCollector implements Refiner {

    protected final OpenApiClient openApiClient;
    private final TitleDuplicateChecker titleDuplicateChecker;

    @Override
    public List<Content> refine() {
        // 여기에 추상 메소드들 기반으로 로직 처리처리할 예정
        // 생성한 것들을 바탕으로 프로세스까지 처리하는 걸 여기에 책임을 부여하자(팩토리 메서드 취지)
        List<RefinedDataDTO> data = retry(this::collectData);

        return data.stream()
                .filter(e -> titleDuplicateChecker.isDuplicate(e.title()))
                .map(Content::create).toList();
    }

    // 각 OpenAPI 호출과 관련된 추상메소드들
    public abstract List<RefinedDataDTO> collectData();

    // 재시도 로직
    protected List<RefinedDataDTO> retry(CheckedSupplier<List<RefinedDataDTO>> supplier) {
        final int MAX_RETRY = 3;

        for (int i = 1; i <= MAX_RETRY; i++) {
            try {
                return supplier.get(); // 실질적으로 collectData()가 동작하는 곳
            } catch (Exception e) {
                log.warn("재시도 {}/{} 실패", i, MAX_RETRY);
            }
        }

        return List.of(); // 모든 시도 실패
    }
}
