package org.jun.saemangeum.service.api;

import org.jun.saemangeum.process.application.service.api.FestivalCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FestivalCollectorTest {

    @Autowired
    private FestivalCollector festivalCollector;

    @Test
    @DisplayName("공공데이터 축제 정보 정제 테스트")
    void test() {
        List<RefinedDataDTO> data = festivalCollector.collectData();

        System.out.println(data);

        Assertions.assertEquals(40, data.size());
    }
}
