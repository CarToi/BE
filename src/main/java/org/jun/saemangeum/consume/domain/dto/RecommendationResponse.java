package org.jun.saemangeum.consume.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jun.saemangeum.global.domain.Category;

@Getter
@AllArgsConstructor
public class RecommendationResponse {
    private String title; // 추천 컨텐츠명
    private String position; // 위치 (주소 혹은 추상적인 지리적 위치)
    private Category category; // 컨텐츠 분류 (FESTIVAL, EVENT, TOUR, CULTURE)
    private String image; // 이미지 소스 url (null 가능성 존재)
    private String url; // 컨텐츠 데이터 출처
    private Coordinate coordinate; // 위경도 좌표 내부 JSON

    public void updateCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
