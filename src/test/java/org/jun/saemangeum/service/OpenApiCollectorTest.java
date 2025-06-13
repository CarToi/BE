package org.jun.saemangeum.service;

import org.jun.saemangeum.process.application.collect.api.GunsanCultureCollector;
import org.jun.saemangeum.process.application.collect.api.SmgEventCollector;
import org.jun.saemangeum.process.application.collect.api.SmgFestivalCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OpenApiCollectorTest {

    @Autowired
    private SmgFestivalCollector smgFestivalCollector;

    @Autowired
    private SmgEventCollector smgEventCollector;

    @Autowired
    private GunsanCultureCollector gunsanCultureCollector;

    @Test
    @DisplayName("공공데이터 축제 정보 정제 테스트")
    void testFestivalCollector() {
        List<RefinedDataDTO> data = smgFestivalCollector.collectData();
        System.out.println(data);

        Assertions.assertEquals(40, data.size());
    }

    @Test
    @DisplayName("공공데이터 공연행사 정보 정제 테스트")
    void testEventCollector() {
        List<RefinedDataDTO> data = smgEventCollector.collectData();
        System.out.println(data);

        Assertions.assertEquals(51, data.size());
    }

    @Test
    @DisplayName("공공데이터 군산시 문화시설 정보 정제 테스트")
    void testGunsanCultureCollector() {
        List<RefinedDataDTO> data = gunsanCultureCollector.collectData();
        System.out.println(data);

        Assertions.assertEquals(50, data.size());
    }
}
