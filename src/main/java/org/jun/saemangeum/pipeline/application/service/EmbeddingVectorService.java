package org.jun.saemangeum.pipeline.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.service.ContentService;
import org.jun.saemangeum.global.service.VectorService;
import org.jun.saemangeum.pipeline.application.dto.RefinedDataDTO;
import org.jun.saemangeum.pipeline.infrastructure.api.VectorClient;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingVectorService {

    private final VectorClient vectorClient;
    private final VectorService vectorService;
    private final ContentService contentService;

    // AI 전처리 로직
    public void embeddingVector(RefinedDataDTO dto) {
        String text = dto.title() + " " + dto.introduction();

        // 설명 뒷부분 일부를 잘라서라도 토큰 조건 맞추기
        if (text.length() > 800) {
            log.info("길이 증가한 놈: \n{}", text);
            String[] sentences = text.split("(?<=[.!?\\n])");
            StringBuilder sb = new StringBuilder();
            for (String sentence : sentences) {
                if (sb.length() + sentence.length() > 800) break;
                sb.append(sentence);
            }

            text = sb.toString().trim();
        };

        EmbeddingResponse response = vectorClient.getWithRaw(text);
        byte[] vectorBytes = floatToByte(response);

        // 트랜잭션 시작
        upsertContent(dto, vectorBytes);
        // 트랜잭션 끝
    }

    // 벡터 플롯 타입 배열 -> 바이트 타입 변환 후 저장
    private byte[] floatToByte(EmbeddingResponse response) {
        float[] floats = response.result().embedding();
        ByteBuffer byteBuffer = ByteBuffer.allocate(floats.length * 4);
        byteBuffer.asFloatBuffer().put(floats);
        return byteBuffer.array();
    }

    @Transactional // 트랜잭션 AOP로 감싸기 위한 퍼블릭 조치
    public void upsertContent(RefinedDataDTO dto, byte[] bytes) {
        Content saveContent = contentService.upsertContent(dto);
        saveContent.updateFrom(dto);
        saveContent.upsertVector(bytes);
    }
}
