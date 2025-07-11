package org.jun.saemangeum.pipeline.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.service.VectorService;
import org.jun.saemangeum.pipeline.infrastructure.api.VectorClient;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingVectorService {

    private final VectorClient vectorClient;
    private final VectorService vectorService;

    // AI 전처리 로직
    public void embeddingVector(Content content) {
        String text = content.getTitle() + " " + content.getIntroduction();

        // 설명 뒷부분 일부를 잘라서라도 토큰 조건 맞추기
        if (text.length() > 800) {
            log.info("길이 증가한 놈: {} // \n{}", content.getId(), content.getTitle() + " " + content.getIntroduction());
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
        Vector vector = Vector.builder().vector(vectorBytes).content(content).build();

        content.setVector(vector);
        vectorService.saveVector(vector);
    }

//    // 유사도 계산
//    public List<Content> calculateSimilarity(String text) {
//        EmbeddingResponse response = vectorClient.get(text);
//        float[] requestVec = VectorCalculator.addNoise(response.result().embedding());
//
//        List<Vector> vectors = vectorService.getVectors(); // 이거 캐싱 대상이겠는데?
//        PriorityQueue<ContentSimilarity> pq = new PriorityQueue<>();
//
//        for (Vector vec : vectors) {
//            float[] storedVec = byteToFloat(vec);
//            double similarity = VectorCalculator.cosineSimilarity(requestVec, storedVec);
//
//            ContentSimilarity cs = new ContentSimilarity(vec.getContent(), similarity);
//            if (pq.size() < 10) {
//                pq.offer(cs);
//            } else if (similarity > pq.peek().similarity) {
//                pq.poll();
//                pq.offer(cs);
//            }
//        }
//
//        return pq.stream().sorted(Comparator.reverseOrder()).map(e -> e.content).toList();
//    }

    // 벡터 플롯 타입 배열 -> 바이트 타입 변환 후 저장
    private byte[] floatToByte(EmbeddingResponse response) {
        float[] floats = response.result().embedding();
        ByteBuffer byteBuffer = ByteBuffer.allocate(floats.length * 4);
        byteBuffer.asFloatBuffer().put(floats);
        return byteBuffer.array();
    }

//    // 바이트 타입 필드 조회 -> 벡터 플롯 타입 변환
//    private float[] byteToFloat(Vector vector) {
//        byte[] bytes = vector.getVector();
//        FloatBuffer floatBuffer = ByteBuffer.wrap(bytes).asFloatBuffer();
//        float[] floats = new float[floatBuffer.remaining()];
//        floatBuffer.get(floats);
//        return floats;
//    }
//
//    // 유사도 내부 클래스
//    record ContentSimilarity(Content content, double similarity)
//            implements Comparable<ContentSimilarity> {
//        @Override
//        public int compareTo(ContentSimilarity o) {
//            // 유사도 기준 오름차순 정렬
//            return Double.compare(this.similarity, o.similarity);
//        }
//    }
}
