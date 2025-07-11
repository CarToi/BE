package org.jun.saemangeum.global.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional
    public void saveContents(List<Content> contents) {
        contentRepository.saveAll(contents);
    }

    @Transactional
    public void deleteByCollectSource(CollectSource collectSource) {
        contentRepository.deleteByCollectSource(collectSource);
    }

    @Transactional(readOnly = true)
    public List<Content> getContentsByClientId(String clientId) {
        return contentRepository.findRecommendedContentsByClientId(clientId);
    }

    @Transactional
    public void deleteAll() {
        contentRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Content> getContents() {
        return contentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Content> getContentsByParticularId(List<Long> id) {
        return contentRepository.findAllById(id);
    }
}
