package org.jun.saemangeum.pipeline.application.dto;

import java.util.List;

public record GimjeCultureResponse(
        int page,
        int perPage,
        int totalCount,
        int currentCount,
        int matchCount,
        List<GimjeCulture> data
) {
}
