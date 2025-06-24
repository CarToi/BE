package org.jun.saemangeum.consume.controller;

import org.jun.saemangeum.consume.domain.dto.SurveyCreateRequest;
import org.jun.saemangeum.consume.domain.dto.SurveyUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    @PostMapping("/recommendation")
    public List<?> proposeRecommendedDataList(@RequestBody SurveyCreateRequest request) {
        return null;
    }

    @PutMapping("/update")
    public void updateSurveyResult(@RequestBody SurveyUpdateRequest request) {
    }
}
