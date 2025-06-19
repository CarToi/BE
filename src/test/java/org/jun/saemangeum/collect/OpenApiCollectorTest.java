package org.jun.saemangeum.collect;

import org.jun.saemangeum.process.application.collect.api.GimjeCultureCollector;
import org.jun.saemangeum.process.application.collect.api.GunsanCultureCollector;
import org.jun.saemangeum.process.application.collect.api.SmgEventCollector;
import org.jun.saemangeum.process.application.collect.api.SmgFestivalCollector;
import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
import org.jun.saemangeum.process.application.service.DataCountUpdateService;
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
public class OpenApiCollectorTest {

    @Autowired
    private SmgFestivalCollector smgFestivalCollector;

    @Autowired
    private SmgEventCollector smgEventCollector;

    @Autowired
    private GunsanCultureCollector gunsanCultureCollector;

    @Autowired
    private GimjeCultureCollector gimjeCultureCollector;

    // MockBean 어노테이션이 사라질 거라서 임의 내부 클래스로 설정 처리...
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

    @Test
    @DisplayName("공공데이터 김제시 다중이용시설 정보 정제 테스트")
    void testGimjeCultureCollector() {
        List<RefinedDataDTO> data = gimjeCultureCollector.collectData();
        System.out.println(data);

        Assertions.assertEquals(3, data.size());
    }
}
