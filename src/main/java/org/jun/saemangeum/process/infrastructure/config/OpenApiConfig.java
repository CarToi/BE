package org.jun.saemangeum.process.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${crawling.openApiKey}")
    private String apiKey;

    @Bean(name = "openApiTemplate")
    public RestTemplate openApiTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            URI uri = request.getURI();

            URI updateUri = UriComponentsBuilder.fromUri(uri)
                    .queryParam("serviceKey", apiKey)
                    .build(true) // 인코딩 유지 설정을 통한 중복 인코딩 방지
                    .toUri();

            HttpRequest newRequest = new HttpRequestWrapper(request) {
                @Override
                public URI getURI() {
                    return updateUri;
                }
            };

            return execution.execute(newRequest, body);
        }));

        return restTemplate;
    }

}
