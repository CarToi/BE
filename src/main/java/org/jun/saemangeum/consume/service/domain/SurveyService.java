package org.jun.saemangeum.consume.service.domain;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.entity.Survey;
import org.jun.saemangeum.consume.repository.entity.SurveyRepository;
import org.jun.saemangeum.global.exception.ClientIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public Survey save(Survey survey) {
        return surveyRepository.save(survey);
    }

    public boolean isExistedClientId(String clientId) {
        return surveyRepository.findByClientId(clientId).isPresent();
    }

    public Survey findByClientId(String clientId) {
        return surveyRepository.findByClientId(clientId).orElseThrow(
                // 커스텀 예외로 전환 + 전역 예외 핸들러 처리
                () -> new ClientIdException("해당 클라이언트 아이디가 존재하지 않음")
        );
    }
}
