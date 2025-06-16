package org.jun.saemangeum.process.application.util;

import org.jun.saemangeum.global.domain.CollectSource;
import org.jun.saemangeum.global.domain.Count;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.global.service.CountService;
import org.slf4j.Logger;

public class CollectorUtil {
    public static boolean compareSize(int size, CollectSource collectSource, CountService countService, Logger log, ContentService contentService) {
        Count existedCount = countService.findByCollectSource(collectSource);
        int existingSize = existedCount.getCount();
        if (size != existingSize) {
            log.info("{} 새로운 개수: {} // 기존 개수: {}", collectSource, size, existingSize);
            existedCount.update(size);
            contentService.deleteByCollectSource(collectSource);

            return true;
        }

        return false;
    }
}
