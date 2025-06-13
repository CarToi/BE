package org.jun.saemangeum.process.presentation.dto;

import java.util.List;

public record CultureResponse(
        int page,
        int perPage,
        int totalCount,
        int currentCount,
        int matchCount,
        List<Culture> data
) {
}
