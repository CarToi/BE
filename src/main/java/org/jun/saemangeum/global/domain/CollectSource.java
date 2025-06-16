package org.jun.saemangeum.global.domain;

/**
 * 구분용 합성 코드 : 지명 + 분야(카테고리) + 수집 방법(크롤링: CR, API: API)
 */
public enum CollectSource {
    GJCUAP, // 김제 문하시설 API
    GSCUAP, // 군산 문화시설 API
    SMGEVAP, // 새만금 행사 API
    SMGFEAP, // 새만금 축제 API
    ARTOCR, // 새만금 군도 관광지 크롤링
    GJTOCR, // 김제 관광지 크롤링
    GSTOCR, // 군산 관광지 크롤링
    BATOCR, // 부안 관광지 크롤링
    GSFECR, // 군산 축제 크롤링
    SWTOCR, // 새만금 방조제 관광지 크롤링
}
