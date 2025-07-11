package org.jun.saemangeum.pipeline.infrastructure.api;

import org.jun.saemangeum.global.cache.CacheNames;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingRequest;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VectorClient {

    @Value("${ai.baseUrl}")
    private String baseUrl;

    private final RestTemplate vectorTemplate;

    public VectorClient(RestTemplate vectorTemplate) {
        this.vectorTemplate = vectorTemplate;
    }

    // 너도 캐싱대상
    @Cacheable(cacheNames = CacheNames.EMBEDDING, key = "#text")
    public EmbeddingResponse getWithCache(String text) {
        return getWithRaw(text);
    }

    public EmbeddingResponse getWithRaw(String text) {
        EmbeddingRequest request = new EmbeddingRequest(text);
        return vectorTemplate.postForObject(baseUrl, request, EmbeddingResponse.class);
    }
}
