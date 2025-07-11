package org.jun.saemangeum.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jun.saemangeum.global.cache.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    // global.service.VectorService.getVectors()
    // pipeline.infrastructure.api.VectorClient.getWithCache()
    // consume.service.application.SurveyRecommendationService.getSurveyRecommendationResults()

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(c -> new CaffeineCache(c.getCacheName(),
                        Caffeine.newBuilder()
                                .recordStats().expireAfterWrite(c.getExpiredAfterWrite(), TimeUnit.MINUTES)
                                .maximumSize(c.getMaximumSize())
                                .build()))
                .toList();

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
