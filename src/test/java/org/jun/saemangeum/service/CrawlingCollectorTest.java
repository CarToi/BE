package org.jun.saemangeum.service;

import org.jun.saemangeum.process.application.service.crawl.GunsanTourCollector;
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
    private GunsanTourCollector gunsanTourCollector;

    @Test
    @DisplayName("새만금 개발청 방조제 정보 크롤링 테스트")
    void testSeawallTourCollector() {
        List<RefinedDataDTO> data = seawallTourCollector.collectData();
        Assertions.assertEquals(data.size(), 9);
    }

    @Test
    @DisplayName("새만금 개발청 군산 관광지 크롤링 테스트")
    void testGunsanTourCollector() {
        List<RefinedDataDTO> data = gunsanTourCollector.collectData();
        Assertions.assertEquals(data.size(), 8);
    }
}
