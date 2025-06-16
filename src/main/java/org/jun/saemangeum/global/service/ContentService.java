package org.jun.saemangeum.global.service;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.repository.ContentRepository;
import org.jun.saemangeum.global.domain.CollectSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public int countByCollectSource(CollectSource collectSource) {
        return contentRepository.countByCollectSource(collectSource);
    }

    @Transactional(readOnly = true)
    public List<Content> getContents() {
        return contentRepository.findAll();
    }
}
