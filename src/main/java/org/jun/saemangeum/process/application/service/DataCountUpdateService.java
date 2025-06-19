package org.jun.saemangeum.process.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.global.service.CountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataCountUpdateService {

    private final CountService countService;
    private final ContentService contentService;

    @Transactional
    public boolean isNeedToUpdate(int size, CollectSource collectSource) {
        Count existedCount = countService.findByCollectSource(collectSource);
        int existingSize = existedCount.getCount();
        if (size != existingSize) {
            log.info("{} 새로운 개수: {} // 기존 개수: {}", collectSource, size, existingSize);
            existedCount.update(size);
            contentService.deleteByCollectSource(collectSource);

            return true;
        }

        log.info("{} 데이터 카운팅 업데이트 없음", collectSource);
        return false;
    }
}
