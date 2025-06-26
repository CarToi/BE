package org.jun.saemangeum.collect;

import org.jun.saemangeum.global.domain.Category;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.pipeline.application.collect.base.CrawlingCollector;
import org.jun.saemangeum.pipeline.application.collect.base.OpenApiCollector;
import org.jun.saemangeum.pipeline.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class TitleDuplicateCheckerTest {

    @Test
    @DisplayName("타이틀 기반 수집 데이터 중복 필터링 체크, 숫자와 공백 배제")
    void testTitleDuplicateChecker() {
        TitleDuplicateChecker checker = new TitleDuplicateChecker();

        OpenApiCollector apiCollector = new OpenApiCollector(null, null, checker) {
            @Override
            public List<RefinedDataDTO> collectData() {
                return List.of(
                        new RefinedDataDTO("축제 1", "군산", Category.EVENT, "", "", "", null),
                        new RefinedDataDTO("축제1", "군산", Category.EVENT, "", "", "", null),
                        new RefinedDataDTO("축제2", "부안", Category.TOUR, "", "", "", null)
                );
            }
        };

        CrawlingCollector crawlingCollector = new CrawlingCollector(null, checker) {
            @Override
            public List<RefinedDataDTO> collectData() {
                return List.of(
                        new RefinedDataDTO("1 행사", "군산", Category.TOUR, "", "", "", null),
                        new RefinedDataDTO("행사1", "김제", Category.EVENT, "", "", "", null),
                        new RefinedDataDTO("축제2", "부안", Category.FESTIVAL, "", "", "", null)
                );
            }
        };

        List<Content> result1 = apiCollector.refine(); // "축제 1" 먼저 저장
        List<Content> result2 = crawlingCollector.refine(); // "1 행사" 먼저 저장
        List<Content> result = Stream.concat(result1.stream(), result2.stream()).toList();

        Assertions.assertEquals(result.size(), 2);
        assertThat(result).extracting(Content::getTitle).containsExactlyInAnyOrder("축제 1", "1 행사");

        checker.reset();
    }
}
