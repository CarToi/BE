package org.jun.saemangeum.process.dto;

import java.util.List;

public record FestivalResponse(
        int page,
        int perPage,
        int totalCount,
        int currentCount,
        int matchCount,
        List<Festival> data
) {
}
