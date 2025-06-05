package org.jun.saemangeum.peristalsis;

import org.jun.saemangeum.dto.FestivalResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class OpenApiTest {

    @Autowired
    private WebClient webClient;

    @Test
    @DisplayName("공공데이터 Open API 작동 테스트")
    void test() {
        FestivalResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/15006172/v1/uddi:070933dc-7dcc-4aca-8a3b-882c34de1707_201908211747")
                        .queryParam("page", 1)
                        .queryParam("perPage", 10)
                        .build())
                .retrieve()
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
//                        clientResponse.bodyToMono(ErrorResponse.class).flatMap(body ->
//                                Mono.error(new IllegalArgumentException(body.msg())))
//                )
//                .onStatus(HttpStatusCode::is5xxServerError,  clientResponse ->
//                        clientResponse.bodyToMono(ErrorResponse.class).flatMap(body ->
//                                Mono.error(new IllegalArgumentException(body.msg())))
//                )
                .bodyToMono(FestivalResponse.class)
                .block();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(8, response.data().size());
    }
}
