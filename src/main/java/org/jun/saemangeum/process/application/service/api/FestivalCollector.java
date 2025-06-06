package org.jun.saemangeum.process.application.service.api;

import org.jun.saemangeum.process.application.service.base.OpenApiCollector;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.jun.saemangeum.process.infrastructure.api.RestTemplateClient;
import org.jun.saemangeum.process.presentation.dto.Festival;
import org.jun.saemangeum.process.presentation.dto.FestivalResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 공공데이터 API 축제 CSV 받아오기
 */
@Component
public class FestivalCollector extends OpenApiCollector {

    private static final String LAST_PATH = "/15006172/v1/uddi:ede8925d-bfbd-49fc-9f3c-abf1ead5b402";

    public FestivalCollector(RestTemplateClient restTemplateClient) {
        super(restTemplateClient);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        FestivalResponse response = restTemplateClient.get(
                LAST_PATH,
                FestivalResponse.class,
                q -> q.queryParam("page", 1).queryParam("perPage", 100)
        );

        return response.data().stream().map(Festival::convertToDTO).toList();
    }

}
