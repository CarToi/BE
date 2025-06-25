package org.jun.saemangeum.consume.domain.dto;

import java.util.List;

public record SurveyUpdateRequest(
        String clientId,
        List<Integer> satisfaction
) {
}
