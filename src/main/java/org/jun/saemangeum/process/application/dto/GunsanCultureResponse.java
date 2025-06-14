package org.jun.saemangeum.process.application.dto;

import java.util.List;

public record GunsanCultureResponse(
        int page,
        int perPage,
        int totalCount,
        int currentCount,
        int matchCount,
        List<GunsanCulture> data
) {
}
