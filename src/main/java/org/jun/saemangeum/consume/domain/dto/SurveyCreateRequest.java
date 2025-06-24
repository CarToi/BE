package org.jun.saemangeum.consume.domain.dto;

public record SurveyCreateRequest(
        String uuid,
        int age,
        String gender,
        String city,
        String want,
        String mood
) {
}
