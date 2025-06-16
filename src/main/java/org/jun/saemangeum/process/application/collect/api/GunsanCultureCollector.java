package org.jun.saemangeum.process.application.collect.api;

import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.global.service.CountService;
import org.jun.saemangeum.process.application.collect.base.OpenApiCollector;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.process.application.service.DataCountUpdateService;
import org.jun.saemangeum.process.application.util.TitleDuplicateChecker;
import org.jun.saemangeum.process.application.dto.RefinedDataDTO;
import org.jun.saemangeum.process.infrastructure.api.OpenApiClient;
import org.jun.saemangeum.process.application.dto.GunsanCultureResponse;
import org.springframework.stereotype.Service;

import java.util.List;

// 업데이트 로직 통과
/**
 * 공공데이터 군산 문화시설 API
 */
@Service
public class GunsanCultureCollector extends OpenApiCollector {

    private static final String LAST_PATH = "/15041531/v1/uddi:282417f2-01d9-4bff-9d8c-dcc80d78b20e";
    private static final String URL = "https://www.ktriptips.com/kor/culture?&s_ac=37&s_sc=2";

    public GunsanCultureCollector(
            OpenApiClient openApiClient,
            DataCountUpdateService dataCountUpdateService,
            TitleDuplicateChecker titleDuplicateChecker) {
        super(openApiClient, dataCountUpdateService, titleDuplicateChecker);
    }

    @Override
    public List<RefinedDataDTO> collectData() {
        GunsanCultureResponse response = openApiClient.get(
                LAST_PATH,
                GunsanCultureResponse.class,
                q -> q.queryParam("page", 1).queryParam("perPage", 100)
        );

        if (dataCountUpdateService.isNeedToUpdate(response.totalCount(), CollectSource.GSCUAP))
            return response.data().stream().map(e -> RefinedDataDTO.to(e, URL, CollectSource.GSCUAP)).toList();

        return List.of();
    }
}
