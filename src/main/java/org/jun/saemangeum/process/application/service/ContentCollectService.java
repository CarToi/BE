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
                .map(refiner -> refiner.stream().peek(
                        e -> log.info("각 구현체들 전처리 예정")
                ).toList())
                .forEach(contentRepository::saveAll);
    }
    @Transactional
    public void collectAndSaveByParallelStream() {
        refiners.parallelStream()
                .map(Refiner::refine)
                .map(refiner -> refiner.stream().peek(
                        e -> log.info("각 구현체들 전처리 예정")
                ).toList())
                .forEach(contentRepository::saveAll);
    }

    @Transactional(readOnly = true)
    public List<Content> getContents() {
        return contentRepository.findAll();
    }
}
