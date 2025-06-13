package org.jun.saemangeum.process.application.collect.api;

import org.jun.saemangeum.process.application.collect.base.OpenApiCollector;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.domain.dto.RefinedDataDTO;
import org.jun.saemangeum.process.infrastructure.api.OpenApiClient;
import org.jun.saemangeum.process.presentation.dto.GimjeCultureResponse;
import org.jun.saemangeum.process.presentation.dto.GunsanCultureResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GimjeCultureCollector extends OpenApiCollector {

    private static final String LAST_PATH = "/15041593/v1/uddi:d5397703-2829-4811-a0f6-b02cac614b38";
    private static final String URL = "https://www.gimje.go.kr/index.gimje?menuCd=DOM_000000106011003000";

    public GimjeCultureCollector(
            OpenApiClient openApiClient, TitleDuplicateChecker titleDuplicateChecker) {
        super(openApiClient, titleDuplicateChecker);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        GimjeCultureResponse response = openApiClient.get(
                LAST_PATH,
                GimjeCultureResponse.class,
                q -> q.queryParam("page", 1).queryParam("perPage", 100)
        );

        return response.data().stream().map(e -> RefinedDataDTO.to(e, URL)).toList();
    }

}
