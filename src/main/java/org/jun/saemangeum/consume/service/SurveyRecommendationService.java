package org.jun.saemangeum.consume.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.dto.AverageRequest;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class SurveyRecommendationService {
    private final EmbeddingVectorService embeddingVectorService;
    private final SurveyService surveyService;
    private final RecommendationLogService recommendationLogService;

    /**
     * 사용자 설문응답 문자열을 일괄 묶어 임베딩 벡터 처리 후, 로그 확보 + 추천 리스트 반환
     */
    public List<RecommendationResponse> createRecommendationsBySurvey(SurveyCreateRequest request) {
        String age = request.age() > 30 ? "늙은" : "젊은";
        String text = request.gender() + " " + age + " "
                + request.city() + " " + request.mood() + " " + request.want();

        // 이 부분이 전략 패턴이 적용되어야 할듯?
        List<Content> contents = embeddingVectorService.calculateSimilarity(text);
        Survey survey =  surveyService.save(Survey.create(request));

        List<RecommendationLog> recommendationLogs = contents.stream()
                .map(e -> new RecommendationLog(e, survey)).toList();
        recommendationLogService.saveALl(recommendationLogs);

        return contents.stream().map(Content::to).toList();
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
            throw new RuntimeException("잘못된 만족도 조사 요청 양식");
        }

        survey.updateEvaluation(satisfaction.get(0), satisfaction.get(1), satisfaction.get(2));
    }

//    /**
//     * 설문 조합에 따른 평균 컨텐츠 추천 수 반환 -> 통게용으로만 활용 예정
//     */
//    @Transactional
//    public List<RecommendationResponse> calculateAverageSurvey(AverageRequest request) {
//
//        // 1. 설문 응답 조합(연령, 성별, 거주의향지, 여유 선호, 분위기 선호)에 해당하는 뷰 중 id 혹은 uuid 리스트 추출
//        // 2. 1번에서 얻은 리스트에 해당하는 로그 리스트로부터 컨텐츠 id들 조회
//        // 3. 2번에서 컨텐츠 id 출현 빈도 연산해서 상위 10개 산출
//
//        // 조합이 없으면 직접 해당 요소들로 벡터 임베딩을 진행해 나온 결과를 반환한다(폴백) -> 사용자 기능 확장시 고려
//
//        List<Long> ids = recommendationLogService.getRecommendationLogIdsJoinSurveys(request);
//
//        // 여기서 조건 분기 가르기? ids 사이즈에 따라? 그리고 캐싱 처리 too? -> 사용자 기능 확장시 고려
//
//        List<Long> top10Ids = getTop10Frequent(ids);
//
//        // 사실 이 부분도 전략패턴 적용이어야 하지만... 일단 삭제
//        return contentService.getContentsByParticularId(top10Ids).stream()
//                .map(Content::to).toList();
//    }
//
//    // 10개의 조회 상위 빈도수 리스트 반환
//    private List<Long> getTop10Frequent(List<Long> numbers) {
//        // 빈도수 계산
//        Map<Long, Integer> frequencyMap = new HashMap<>();
//        for (Long num : numbers) {
//            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
//        }
//
//        // 최소 힙(빈도 오름차순)
//        PriorityQueue<Map.Entry<Long, Integer>> minHeap =
//                new PriorityQueue<>(Map.Entry.comparingByValue());
//
//        // 상위 10개 유지
//        for (Map.Entry<Long, Integer> entry : frequencyMap.entrySet()) {
//            minHeap.offer(entry);
//            if (minHeap.size() > 10) {
//                minHeap.poll(); // 가장 적은 빈도 제거
//            }
//        }
//
//        // 결과 추출 (빈도 높은 순서로 역정렬)
//        List<Long> result = new ArrayList<>();
//        while (!minHeap.isEmpty()) {
//            result.add(minHeap.poll().getKey());
//        }
//        Collections.reverse(result); // 높은 빈도 순으로 정렬
//        return result;
//    }
}
