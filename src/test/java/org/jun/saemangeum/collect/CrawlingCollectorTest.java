package org.jun.saemangeum.collect;

import org.jun.saemangeum.pipeline.application.collect.crawl.*;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.jun.saemangeum.pipeline.application.service.DataCountUpdateService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CrawlingCollectorTest {

    @Autowired
    private SeawallTourCollector seawallTourCollector;

    @Autowired
    private CityTourCollector cityTourCollector;

    @Autowired
    private ArchipelagoCollector archipelagoCollector;

    @Autowired
    private GunsanFestivalCollector gunsanFestivalCollector;

    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        @Primary
        public DataCountUpdateService mockDataCountUpdateService() {
            DataCountUpdateService mock = Mockito.mock(DataCountUpdateService.class);
            Mockito.when(mock.isNeedToUpdate(Mockito.anyInt(), Mockito.any())).thenReturn(true);
            return mock;
        }
    }

    @Test
    @DisplayName("새만금 개발청 방조제 정보 크롤링 테스트")
    void testSeawallTourCollector() throws Exception {
        List<RefinedDataDTO> data = seawallTourCollector.collectData();
        Assertions.assertEquals(data.size(), 9);
    }

    @Test
    @DisplayName("새만금 개발청 도시 관광지 크롤링 테스트")
    void testCityTourCollector() throws Exception {
        List<RefinedDataDTO> data = cityTourCollector.collectData();
        Assertions.assertEquals(data.size(), 41);
    }

    @Test
    @DisplayName("새만금 개발공사 군도 관광지 크롤링 테스트")
    void testArchipelagoCollector() throws Exception {
        List<RefinedDataDTO> data = archipelagoCollector.collectData();
        Assertions.assertEquals(data.size(), 6);
    }

    @Test
    @DisplayName("군산시 문화관광 축제행사 크롤링 테스트")
    void testGunsanFestivalCollector() throws Exception {
        List<RefinedDataDTO> data = gunsanFestivalCollector.collectData();
        Assertions.assertEquals(data.size(), 10);
    }

//    // 아니 사이트가 인증서 설정 신뢰성이 없는 게 말이 돼...?
//    @Test
//    @DisplayName("부안시 문화시설 크롤링 테스트")
//    void testBuanCultureCollector() throws Exception {
//        List<RefinedDataDTO> data = buanCultureCollector.collectData();
//        Assertions.assertEquals(data.size(), 4);
//    }
}
