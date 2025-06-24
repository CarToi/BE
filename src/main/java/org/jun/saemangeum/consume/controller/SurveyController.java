package org.jun.saemangeum.consume.controller;

import org.jun.saemangeum.consume.domain.dto.AverageRequest;
import org.jun.saemangeum.consume.domain.dto.RecommendationResponse;
import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;
import org.jun.saemangeum.consume.domain.dto.SurveyUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    /**
     * 사용자 설문 결과 추천 응답 데이터 리스트 반환
     */
    @PostMapping("/recommendation")
    public List<RecommendationResponse> createSurvey(@RequestBody SurveyCreateRequest request) {
        return null;
    }

    /**
     * 사용자 설문 결과 만족도 반영 요청
     */
    @PatchMapping("/update")
    public void updateSurvey(@RequestBody SurveyUpdateRequest request) {
    }

    /**
     * 평균 사용자 설문 응답 확인 응답 데이터 리스트 반환
     */
    @GetMapping("/average")
    public List<RecommendationResponse> readAverageSurvey(@RequestBody AverageRequest request) {
        return null;
    }
}
