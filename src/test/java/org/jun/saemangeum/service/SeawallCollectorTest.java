package org.jun.saemangeum.service;

import org.jun.saemangeum.process.application.service.crawl.SeawallCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SeawallCollectorTest {

    @Autowired
    private SeawallCollector seawallCollector;

    @Test
    @DisplayName("새만금 개발청 방조제 정보 크롤링 테스트")
    void test() {
        seawallCollector.collectData();
    }
}
