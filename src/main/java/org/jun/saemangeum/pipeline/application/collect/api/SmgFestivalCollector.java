package org.jun.saemangeum.pipeline.application.collect.api;

import org.jun.saemangeum.pipeline.application.collect.base.OpenApiCollector;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.pipeline.application.service.DataCountUpdateService;
import org.jun.saemangeum.pipeline.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.jun.saemangeum.pipeline.infrastructure.api.OpenApiClient;
import org.jun.saemangeum.pipeline.application.dto.FestivalResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 공공데이터 API 축제 CSV 받아오기
 */
@Component
public class SmgFestivalCollector extends OpenApiCollector {

    private static final String LAST_PATH = "/15006172/v1/uddi:ede8925d-bfbd-49fc-9f3c-abf1ead5b402";
    private static final String URL = "https://www.saemangeum.go.kr/sda/content.do?key=2010083672101";

    public SmgFestivalCollector(
            OpenApiClient openApiClient,
            DataCountUpdateService dataCountUpdateService,
            TitleDuplicateChecker titleDuplicateChecker) {
        super(openApiClient, dataCountUpdateService, titleDuplicateChecker);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        FestivalResponse response = openApiClient.get(
                LAST_PATH,
                FestivalResponse.class,
                q -> q.queryParam("page", 1).queryParam("perPage", 100)
        );

        if (dataCountUpdateService.isNeedToUpdate(response.totalCount(), CollectSource.SMGFEAP))
            return response.data().stream().map(f -> RefinedDataDTO.to(f, URL, CollectSource.SMGFEAP)).toList();

        return List.of();
    }

}
