package org.jun.saemangeum.global.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheExpirePolicy {

    private final CacheManager cacheManager;

    @Scheduled(cron = "0 0 3 ? * SUN", zone = "Asia/Seoul")
    public void evictWeeklyCache() {
        log.info("벡터 데이터리스트 캐시 & 컨텐츠 문자열 전처리 키 기반 캐시 무효화");

        if (cacheManager.getCache(CacheNames.VECTORS) != null) {
            cacheManager.getCache(CacheNames.VECTORS).clear();
            log.info("벡터 데이터리스트 캐시 만료 처리");
        }

        if (cacheManager.getCache(CacheNames.EMBEDDING) != null) {
            cacheManager.getCache(CacheNames.EMBEDDING).clear();
            log.info("컨텐츠 문자열 전처리 키 기반 캐시 만료 처리");
        }
    }
}
