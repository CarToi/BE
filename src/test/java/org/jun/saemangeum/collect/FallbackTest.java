package org.jun.saemangeum.collect;

import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.pipeline.application.collect.base.CrawlingCollector;
import org.jun.saemangeum.pipeline.application.collect.base.OpenApiCollector;
import org.jun.saemangeum.pipeline.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.jun.saemangeum.pipeline.infrastructure.api.OpenApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FallbackTest {

    @Autowired
    private OpenApiClient openApiClient;

    private TitleDuplicateChecker titleChecker;

    @BeforeEach
    void setUp() {
        titleChecker = mock(TitleDuplicateChecker.class);
        when(titleChecker.isDuplicate(any())).thenReturn(true); // 필터 통과
    }

    @Test
    @DisplayName("크롤링 과정에서 3번 재시도 결과 빈 배열을 반환하게 됨")
    void testCrawlingFallbackRetry() {
        CrawlingCollector crawlingCollector = new CrawlingCollector(null, titleChecker) {
            @Override
            public List<RefinedDataDTO> collectData() throws IOException {
                throw new IOException("임의의 입출력 예외 발생");
            }
        };

        List<Content> result = crawlingCollector.refine();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "3번 재시도 실패 후 빈 리스트 반환해야 함");
    }

    @Test
    @DisplayName("크롤링 과정에서 2번 실패해도 3번째에 성공하면 정상 결과를 반환하게 됨")
    void testCrawlingFinallySuccess() {
        RefinedDataDTO mockDto = new RefinedDataDTO(
                "임의의 제목", "주소", null, "이미지", "설명", "url", null);
        List<RefinedDataDTO> mockList = List.of(mockDto);

        // Wrapper class
        class CountingCrawlingCollector extends CrawlingCollector {
            int callCount = 0;

            public CountingCrawlingCollector(TitleDuplicateChecker checker) {
                super(null, checker);
            }

            @Override
            public List<RefinedDataDTO> collectData() throws IOException {
                callCount++;
                if (callCount < 3) {
                    throw new IOException("실패 " + callCount);
                }
                return mockList;
            }
        }

        CountingCrawlingCollector collector = new CountingCrawlingCollector(titleChecker);
        List<Content> result = collector.refine();

        assertEquals(3, collector.callCount);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("임의의 제목", result.getFirst().getTitle());
    }

    @Test
    @DisplayName("API 호출에서 3번 재시도 결과 빈 배열을 반환하게 됨")
    void testApiFallbackRetry() {
        OpenApiCollector openApiCollector = new OpenApiCollector(openApiClient, null, titleChecker) {
            @Override
            public List<RefinedDataDTO> collectData() {
                throw new RestClientException("임의의 RestClient 예외 발생");
            }
        };

        List<Content> result = openApiCollector.refine();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "3번 재시도 실패 후 빈 리스트 반환해야 함");
    }

    @Test
    @DisplayName("API 호출 과정에서 2번 실패해도 3번째에 성공하면 정상 결과를 반환하게 됨")
    void testApiFinallySuccess() {
        RefinedDataDTO mockDto = new RefinedDataDTO(
                "임의의 제목", "주소", null, "이미지", "설명", "url", null);
        List<RefinedDataDTO> mockList = List.of(mockDto);

        // Wrapper class
        class CountingOpenApiCollector extends OpenApiCollector {
            int callCount = 0;

            public CountingOpenApiCollector(
                    OpenApiClient client, TitleDuplicateChecker checker) {
                super(client, null, checker);
            }

            @Override
            public List<RefinedDataDTO> collectData() {
                callCount++;
                if (callCount < 3) {
                    throw new RestClientException("실패 " + callCount);
                }
                return mockList;
            }
        }

        CountingOpenApiCollector collector =
                new CountingOpenApiCollector(openApiClient, titleChecker);
        List<Content> result = collector.refine();

        assertEquals(3, collector.callCount);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("임의의 제목", result.getFirst().getTitle());
    }
}
