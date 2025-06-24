package org.jun.saemangeum.pipeline.application.collect.api;

import org.jun.saemangeum.pipeline.application.collect.base.OpenApiCollector;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.pipeline.application.service.DataCountUpdateService;
import org.jun.saemangeum.pipeline.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.jun.saemangeum.pipeline.infrastructure.api.OpenApiClient;
import org.jun.saemangeum.pipeline.application.dto.GimjeCultureResponse;
import org.springframework.stereotype.Service;

import java.util.List;

// 업데이트 로직 통과
@Service
public class GimjeCultureCollector extends OpenApiCollector {

    private static final String LAST_PATH = "/15041593/v1/uddi:d5397703-2829-4811-a0f6-b02cac614b38";
    private static final String URL = "https://www.gimje.go.kr/index.gimje?menuCd=DOM_000000106011003000";

    public GimjeCultureCollector(
            OpenApiClient openApiClient,
            DataCountUpdateService dataCountUpdateService,
            TitleDuplicateChecker titleDuplicateChecker) {
        super(openApiClient, dataCountUpdateService, titleDuplicateChecker);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        GimjeCultureResponse response = openApiClient.get(
                LAST_PATH,
                GimjeCultureResponse.class,
                q -> q.queryParam("page", 1).queryParam("perPage", 100)
        );

        if (dataCountUpdateService.isNeedToUpdate(response.totalCount(), CollectSource.GJCUAP)) {
            return response.data().stream().map(e -> RefinedDataDTO.to(e, URL, CollectSource.GJCUAP)).toList();
        }

        return List.of();
    }
}
