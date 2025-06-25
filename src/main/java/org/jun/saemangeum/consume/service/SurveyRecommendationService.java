package org.jun.saemangeum.consume.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;
import org.jun.saemangeum.consume.domain.dto.SurveyUpdateRequest;
import org.jun.saemangeum.consume.domain.entity.RecommendationLog;
import org.jun.saemangeum.consume.domain.entity.Survey;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.pipeline.application.service.EmbeddingVectorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyRecommendationService {

    // 1. 설문 응답 조합(연령, 성별, 거주의향지, 여유 선호, 분위기 선호)에 해당하는 뷰 중 id 혹은 uuid 리스트 추출
    // 2. 1번에서 얻은 리스트에 해당하는 로그 리스트로부터 컨텐츠 id들 조회
    // 3. 2번에서 컨텐츠 id 출현 빈도 연산해서 상위 10개 산출
    private final EmbeddingVectorService embeddingVectorService;
    private final ContentService contentService;
    private final SurveyService surveyService;
    private final RecommendationLogService recommendationLogService;

    /**
     * 사용자 설문응답 문자열을 일괄 묶어 임베딩 벡터 처리 후, 로그 확보 + 추천 리스트 반환
     */
    public List<RecommendationResponse> createRecommendationsBySurvey(SurveyCreateRequest request) {
        String age = request.age() > 30 ? "늙은" : "젊은";
        String text = request.gender() + " " + age + " "
                + request.city() + " " + request.mood() + " " + request.want();

        List<Content> contents = embeddingVectorService.calculateSimilarity(text);
        Survey survey =  surveyService.save(Survey.create(request));

        List<RecommendationLog> recommendationLogs = contents.stream()
                .map(e -> new RecommendationLog(e, survey)).toList();
        recommendationLogService.saveALl(recommendationLogs);

        return contents.stream().map(Content::to).toList();
    }

    @Transactional
    public void updateSurvey(SurveyUpdateRequest request) {
        Survey survey = surveyService.findByClientId(request.clientId());
        List<Integer> satisfaction = request.satisfactions();

        if (satisfaction.size() < 3) {
            // 커스텀 예외로 전환 + 전역 예외 핸들러 처리
            throw new RuntimeException("잘못된 만족도 조사 요청 양식");
        }

        survey.updateEvaluation(satisfaction.get(0), satisfaction.get(1), satisfaction.get(2));
    }
}
