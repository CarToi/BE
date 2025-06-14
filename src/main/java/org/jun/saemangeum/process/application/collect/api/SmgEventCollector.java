package org.jun.saemangeum.process.application.collect.api;

import org.jun.saemangeum.process.application.collect.base.OpenApiCollector;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
import org.jun.saemangeum.process.infrastructure.api.OpenApiClient;
import org.jun.saemangeum.process.application.dto.EventResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 공공데이터 API 행사 CSV 받아오기
 */
@Service
public class SmgEventCollector extends OpenApiCollector {

    private static final String LAST_PATH = "/15006173/v1/uddi:f353f7f5-589e-4354-9b3e-ef6923273b55";
    private static final String URL = "https://www.saemangeum.go.kr/sda/content.do?key=2010083672101";

    public SmgEventCollector(
            OpenApiClient openApiClient, TitleDuplicateChecker titleDuplicateChecker) {
        super(openApiClient, titleDuplicateChecker);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        EventResponse response = openApiClient.get(
                LAST_PATH,
                EventResponse.class,
                q -> q.queryParam("page", 1).queryParam("perPage", 100)
        );

        return response.data().stream().map(e -> RefinedDataDTO.to(e, URL)).toList();
    }
}
