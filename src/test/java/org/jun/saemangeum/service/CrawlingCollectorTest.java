package org.jun.saemangeum.service;

import org.jun.saemangeum.process.application.service.crawl.ArchipelagoCollector;
import org.jun.saemangeum.process.application.service.crawl.CityTourCollector;
import org.jun.saemangeum.process.application.service.crawl.SeawallTourCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CrawlingCollectorTest {

    @Autowired
    private SeawallTourCollector seawallTourCollector;

    @Autowired
    private CityTourCollector cityTourCollector;

    @Autowired
    private ArchipelagoCollector archipelagoCollector;

    @Test
    @DisplayName("새만금 개발청 방조제 정보 크롤링 테스트")
    void testSeawallTourCollector() {
        List<RefinedDataDTO> data = seawallTourCollector.collectData();
        Assertions.assertEquals(data.size(), 9);
    }

    @Test
    @DisplayName("새만금 개발청 도시 관광지 크롤링 테스트")
    void testCityTourCollector() {
        List<RefinedDataDTO> data = cityTourCollector.collectData();
        Assertions.assertEquals(data.size(), 41);
    }

    @Test
    @DisplayName("새만금 개발공사 군도 관광지 크롤링 테스트")
    void testArchipelagoCollector() {
        List<RefinedDataDTO> data = archipelagoCollector.collectData();
        Assertions.assertEquals(data.size(), 6);
    }
}
