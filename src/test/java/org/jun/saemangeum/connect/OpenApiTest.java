package org.jun.saemangeum.connect;

import org.jun.saemangeum.pipeline.infrastructure.api.OpenApiClient;
import org.jun.saemangeum.pipeline.application.dto.FestivalResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OpenApiTest {

    @Autowired
    private OpenApiClient openApiClient;

    private static final String PATH = "/15006172/v1/uddi:070933dc-7dcc-4aca-8a3b-882c34de1707_201908211747";

    @Test
    @DisplayName("공공데이터 Open API RestTemplate 기반 작동 테스트")
    void testRestTemplate() {
        FestivalResponse response = openApiClient.get(
                PATH,
                FestivalResponse.class,
                q -> q.queryParam("page", 1).queryParam("perPage", 10)
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(8, response.data().size());
    }
}
