package org.jun.saemangeum.consume.domain.dto;

public record AverageRequest(
        int age, // 연령대
        String gender, // 성별
        String resident, // 실거주지
        String city, // 거주의향 지역
        String want, // 여유 선호
        String mood // 분위기 선호
) {
}
