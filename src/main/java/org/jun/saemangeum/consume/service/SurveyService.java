package org.jun.saemangeum.consume.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.repository.SurveyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

}
