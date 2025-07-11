package org.jun.saemangeum.consume.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;

// 엔티티 필드를 어떻게 잡아야 평균값 산출이 쉬우려나...
@Entity
@Builder
@Table(name = "surveys")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", unique = true)
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

    public static Survey create(SurveyCreateRequest request) {
        return Survey.builder()
                .clientId(request.clientId())
                .age(request.age())
                .gender(request.gender())
                .resident(request.resident())
                .city(request.city())
                .want(request.want())
                .mood(request.mood())
                .help(3) // 기본값 3
                .preference(3)
                .intention(3)
                .build();
    }

    public void updateEvaluation(int help, int preference, int intention) {
        this.help = help;
        this.preference = preference;
        this.intention = intention;
    }
}
