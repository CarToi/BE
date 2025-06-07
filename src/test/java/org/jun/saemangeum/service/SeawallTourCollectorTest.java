package org.jun.saemangeum.service;

import org.jun.saemangeum.process.application.service.crawl.SeawallTourCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SeawallTourCollectorTest {

    @Autowired
    private SeawallTourCollector seawallTourCollector;

    @Test
    @DisplayName("새만금 개발청 방조제 정보 크롤링 테스트")
    void test() {
        List<RefinedDataDTO> data = seawallTourCollector.collectData();
        Assertions.assertEquals(data.size(), 9);
    }
}
