package org.jun.saemangeum.pipeline.infrastructure.api;

import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingRequest;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
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

    public EmbeddingResponse get(String text) {
        // 여기서도 캐시가 있어야 될 것 같은데?

        EmbeddingRequest request = new EmbeddingRequest(text);
        return vectorTemplate.postForObject(baseUrl, request, EmbeddingResponse.class);
    }
}
