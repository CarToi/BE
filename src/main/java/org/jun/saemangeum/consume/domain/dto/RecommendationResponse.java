package org.jun.saemangeum.consume.domain.dto;

import org.jun.saemangeum.global.domain.Category;

public record RecommendationResponse(
        String title, // 추천 컨텐츠명
        String position, // 위치 (주소 혹은 추상적인 지리적 위치)
        Category category, // 컨텐츠 분류 (FESTIVAL, EVENT, TOUR, CULTURE)
        String image, // 이미지 소스 url (null 가능성 존재)
        String url // 컨텐츠 데이터 출처
) {
}
