package org.jun.saemangeum.service;

import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.global.service.CountService;
import org.jun.saemangeum.process.application.service.DataCountUpdateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DataUpdateTest {

    private DataCountUpdateService dataCountUpdateService;
    private CollectSource mockCollectSource;

    @BeforeEach
    void setUp() {
        CountService countService = mock(CountService.class);
        ContentService contentService = mock(ContentService.class);
        Count mockCount = mock(Count.class);
        mockCollectSource = mock(CollectSource.class);

        dataCountUpdateService
                = new DataCountUpdateService(countService, contentService);
        when(countService.findByCollectSource(mockCollectSource)).thenReturn(mockCount);
        when(mockCount.getCount()).thenReturn(10);
    }

    @Test
    @DisplayName("업데이트는 데이터 카운트와 수집 대상 갯수 비교를 통해 결정")
    void testDataUpdate() {
        boolean result1 = dataCountUpdateService.isNeedToUpdate(10, mockCollectSource);
        boolean result2 = dataCountUpdateService.isNeedToUpdate(20, mockCollectSource);

        Assertions.assertFalse(result1);
        Assertions.assertTrue(result2);
    }
}
