package org.jun.saemangeum.consume.controller;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.AverageRequest;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;
import org.jun.saemangeum.consume.domain.dto.SurveyUpdateRequest;
import org.jun.saemangeum.consume.service.SurveyRecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyRecommendationService surveyRecommendationService;

    /**
     * 사용자 설문 결과 추천 응답 데이터 리스트 반환
     */
    @PostMapping("/recommendation")
    public List<RecommendationResponse> createSurvey(@RequestBody SurveyCreateRequest request) {
        return surveyRecommendationService.createRecommendationsBySurvey(request);
    }

    /**
     * 사용자 설문 결과 만족도 반영 요청
     */
    @PatchMapping("/update")
    public void updateSurvey(@RequestBody SurveyUpdateRequest request) {
        surveyRecommendationService.updateSurvey(request);
    }

    /**
     * 평균 사용자 설문 응답 확인 응답 데이터 리스트 반환 -> 1차 MVP에서는 사용 x
     */
    @GetMapping("/average")
    public List<RecommendationResponse> readAverageSurvey(@RequestBody AverageRequest request) {
        return surveyRecommendationService.calculateAverageSurvey(request);
    }
}
