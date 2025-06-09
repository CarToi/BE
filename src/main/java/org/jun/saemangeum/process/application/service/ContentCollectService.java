package org.jun.saemangeum.process.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.persistence.domain.Content;
import org.jun.saemangeum.global.persistence.repository.ContentRepository;
import org.jun.saemangeum.process.application.collect.base.Refiner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentCollectService {

    private final List<Refiner> refiners;
    private final ContentRepository contentRepository;
    // ...+ 전처리용 서비스 로직 추가?

    @Transactional
    public void collectAndSave() {
        refiners.stream()
                .map(Refiner::refine)
                .map(refiner -> refiner.stream()
                        .peek(e -> simulateAiPreprocessing())
                        .toList())
                .forEach(contentRepository::saveAll);
    }

    @Transactional
    public void collectAndSaveByParallelStream() {
        refiners.parallelStream()
                .map(Refiner::refine)
                .map(refiner -> refiner.stream()
                        .peek(e -> simulateAiPreprocessing())
                        .toList())
                .forEach(contentRepository::saveAll);
    }

    @Transactional(readOnly = true)
    public List<Content> getContents() {
        return contentRepository.findAll();
    }

    // 실제 AI 연동 전처리 지연 시뮬레이션
    private void simulateAiPreprocessing() {
        try {
            Thread.sleep(100); // 항목당 100ms 지연 (AI 응답 시간 가정)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
