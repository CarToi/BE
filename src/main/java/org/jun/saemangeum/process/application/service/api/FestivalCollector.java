package org.jun.saemangeum.process.application.service.api;

import org.jun.saemangeum.process.application.service.base.OpenApiCollector;
import org.jun.saemangeum.process.application.service.constant.FestivalOpenApiPath;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.jun.saemangeum.process.infrastructure.api.RestTemplateClient;
import org.jun.saemangeum.process.presentation.dto.Festival;
import org.jun.saemangeum.process.presentation.dto.FestivalResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 공공데이터 API 축제 CSV 받아오기
 */
@Component
public class FestivalCollector extends OpenApiCollector {

    public FestivalCollector(RestTemplateClient restTemplateClient) {
        super(restTemplateClient);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        List<Festival> data = new ArrayList<>();

        for (FestivalOpenApiPath endpoint : FestivalOpenApiPath.values()) {
            String path = endpoint.getPath();

            FestivalResponse response = restTemplateClient.get(
                    path,
                    FestivalResponse.class,
                    q -> q.queryParam("page", 1).queryParam("perPage", 100)
            );

            data.addAll(response.data());
        }

        return data.stream().map(Festival::convertToDTO).toList();
    }

}
