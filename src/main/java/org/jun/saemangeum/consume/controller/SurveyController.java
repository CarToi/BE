package org.jun.saemangeum.consume.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;
import org.jun.saemangeum.consume.domain.dto.SurveyUpdateRequest;
import org.jun.saemangeum.consume.service.application.SurveyRecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyRecommendationService surveyRecommendationService;

    /**
     * 사용자 설문 결과 추천 응답 데이터 리스트 생성
     */
    @PostMapping("/recommendation")
    public void createSurvey(@RequestBody SurveyCreateRequest request) {
        log.info("{} - 데이터 소비 POST 요청", Thread.currentThread().getName());
        surveyRecommendationService.createRecommendationsBySurvey(request);
    }

    /**
     * 사용자 설문 결과 추천 응답 데이터 리스트 반환
     */
    @GetMapping
    public List<RecommendationResponse> getSurveyResults(@RequestParam("clientId") String clientId) {
        log.info("{} - 데이터 조회 GET 요청응답", Thread.currentThread().getName());
        return surveyRecommendationService.getSurveyRecommendationResults(clientId);
    }


    /**
     * 사용자 설문 결과 만족도 반영 요청
     */
    @PatchMapping("/update")
    public void updateSurvey(@RequestBody SurveyUpdateRequest request) {
        log.info("{} - 데이터 업데이트 PATCH 요청", Thread.currentThread().getName());
        surveyRecommendationService.updateSurvey(request);
    }
}
