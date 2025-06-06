package org.jun.saemangeum.process.infrastructure.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.function.Consumer;

@Component
public class RestTemplateClient {

    @Value("${dev.baseUrl}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public RestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String path, Class<T> responseType, Consumer<UriComponentsBuilder> extraQuery) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl).path(path);

        if (extraQuery != null) extraQuery.accept(builder);

        URI uri = builder.build(true).toUri();
        return restTemplate.getForObject(uri, responseType);
    }
}
