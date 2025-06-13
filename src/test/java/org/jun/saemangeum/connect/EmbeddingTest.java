package org.jun.saemangeum.connect;

import org.jun.saemangeum.process.infrastructure.api.VectorClient;
import org.jun.saemangeum.process.infrastructure.dto.EmbeddingResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmbeddingTest {

    @Autowired
    private VectorClient vectorClient;

    @Test
    @DisplayName("임의의 텍스트 벡터 임베딩 처리")
    void test() {
        String mockData = "오늘 기분이 좋지 않아";
        EmbeddingResponse embeddingResponse = vectorClient.get(mockData);
        System.out.println(embeddingResponse.result().embedding());
        assertNotNull(embeddingResponse.result().embedding());
    }
}
