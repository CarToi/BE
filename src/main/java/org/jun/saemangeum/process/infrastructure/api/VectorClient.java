package org.jun.saemangeum.process.infrastructure.api;

import org.jun.saemangeum.process.infrastructure.dto.EmbeddingRequest;
import org.jun.saemangeum.process.infrastructure.dto.EmbeddingResponse;
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
        EmbeddingRequest request = new EmbeddingRequest(text);
        return vectorTemplate.postForObject(baseUrl, request, EmbeddingResponse.class);
    }
}
