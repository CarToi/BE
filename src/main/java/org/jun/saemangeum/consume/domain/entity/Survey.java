package org.jun.saemangeum.consume.domain.entity;

// 엔티티 필드를 어떻게 잡아야 평균값 산출이 쉬우려나...
public class Survey {
    private Long id;
    private String clientId; // UUID 클라이언트 id

    // 설문 응답
    private int age; // 연령대
    private String gender; // 성별
    private String resident; // 실거주지
    private String city; // 거주의향 지역
    private String want; // 여유 선호
    private String mood; // 분위기 선호

    // 후속 평가
    private int help; // 도움 정도(1, 2, 3, 4, 5)
    private int preference; // 선호도 정도(1, 2, 3, 4, 5)
    private int intention; // 의향 변화 정도(1, 2, 3, 4, 5)
}
