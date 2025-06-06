package org.jun.saemangeum.process.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Configuration
public class WebClientConfig {

    @Value("${dev.baseUrl}")
    private String baseUrl;

    @Value("${dev.openApiKey}")
    private String apiKey;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter((request, next) -> {
                    URI uri = request.url();

                    URI updateUri = UriComponentsBuilder.fromUri(uri)
                            .queryParam("serviceKey", apiKey)
                            .build(true) // 인코딩 유지 설정을 통한 중복 인코딩 방지
                            .toUri();

                    ClientRequest updatedRequest = ClientRequest.from(request)
                            .url(updateUri)
                            .headers(headers -> headers.addAll(request.headers()))
                            .build();

                    return next.exchange(updatedRequest);
                })
                .build();
    }
}
