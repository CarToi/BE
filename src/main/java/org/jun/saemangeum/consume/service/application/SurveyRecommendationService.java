package org.jun.saemangeum.consume.service.application;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;
import org.jun.saemangeum.consume.domain.dto.SurveyUpdateRequest;
import org.jun.saemangeum.consume.domain.entity.RecommendationLog;
import org.jun.saemangeum.consume.domain.entity.Survey;
import org.jun.saemangeum.consume.service.domain.RecommendationLogService;
import org.jun.saemangeum.consume.service.domain.SurveyService;
import org.jun.saemangeum.consume.service.strategy.StrategyContextHolder;
import org.jun.saemangeum.global.domain.IContent;
import org.jun.saemangeum.global.exception.SatisfactionsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SurveyRecommendationService {
    private final SurveyService surveyService;
    private final RecommendationLogService recommendationLogService;

    /**
     * 사용자 설문응답 문자열을 일괄 묶어 임베딩 벡터 처리 후, 로그 확보 + 추천 리스트 반환
     */
    public List<RecommendationResponse> createRecommendationsBySurvey(SurveyCreateRequest request) {
        String age = request.age() > 30 ? "늙은" : "젊은";
        String text = request.gender() + " " + age + " "
                + request.city() + " " + request.mood() + " " + request.want();

        // 전략 패턴 적용
        List<? extends IContent> contents = StrategyContextHolder.executeStrategy(text);
        Survey survey =  surveyService.save(Survey.create(request));

        List<RecommendationLog> recommendationLogs = contents.stream()
                .map(e -> new RecommendationLog(e, survey)).toList();
        recommendationLogService.saveALl(recommendationLogs);

        return contents.stream().map(IContent::to).toList();
    }

    /**
     * 사용자 설문응답 만족도에 따른 필드 업데이트
     */
    @Transactional
    public void updateSurvey(SurveyUpdateRequest request) {
        Survey survey = surveyService.findByClientId(request.clientId());
        List<Integer> satisfaction = request.satisfactions();

        if (satisfaction.size() < 3) {
            // 커스텀 예외로 전환 + 전역 예외 핸들러 처리
            throw new SatisfactionsException("잘못된 만족도 조사 요청 양식");
        }

        survey.updateEvaluation(satisfaction.get(0), satisfaction.get(1), satisfaction.get(2));
    }
}
