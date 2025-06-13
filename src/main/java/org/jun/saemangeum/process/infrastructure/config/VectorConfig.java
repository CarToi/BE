package org.jun.saemangeum.process.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Configuration
public class VectorConfig {

    @Value("${ai.openApiKey}")
    private String apiKey;

    @Bean(name = "vectorTemplate")
    public RestTemplate vectorTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        restTemplate.getInterceptors().add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", UUID.randomUUID().toString());
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
