package org.jun.saemangeum.consume.domain.dto;

import java.util.List;

public record SurveyUpdateRequest(
        List<Integer> satisfaction
) {
}
