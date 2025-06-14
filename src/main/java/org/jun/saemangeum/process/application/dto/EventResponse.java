package org.jun.saemangeum.process.application.dto;

import java.util.List;

public record EventResponse(
        int page,
        int perPage,
        int totalCount,
        int currentCount,
        int matchCount,
        List<Event> data
) {
}
