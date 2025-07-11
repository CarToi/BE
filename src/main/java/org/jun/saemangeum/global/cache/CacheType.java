package org.jun.saemangeum.global.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {
    // 벡터와 임베딩 캐시는 스케줄러 할당으로 강제 비우기 예정
    VECTORS("vectors", 7 * 24 * 60, 1000), // 7일 만료, 최대 1000개
    EMBEDDING("embedding", 7 * 24 * 60, 1000), // 7일 만료, 최대 1000개
    RESULTS("results", 10, 1000); // 10분 만료, 최대 1000개

    private final String cacheName;
    private final int expiredAfterWrite; // 만료 시간(분 단위)
    private final int maximumSize; // 캐시 최대 크기 (엔트리 수)
}
