package org.jun.saemangeum.process.infrastructure.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.function.Consumer;

@Component
public class OpenApiClient {

    @Value("${dev.baseUrl}")
    private String baseUrl;

    private final RestTemplate openApiTemplate;

    public OpenApiClient(RestTemplate openApiTemplate) {
        this.openApiTemplate = openApiTemplate;
    }

    public <T> T get(String path, Class<T> responseType, Consumer<UriComponentsBuilder> extraQuery) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl).path(path);

        if (extraQuery != null) extraQuery.accept(builder);

        URI uri = builder.build(true).toUri();
        return openApiTemplate.getForObject(uri, responseType);
    }
}
