package org.jun.saemangeum.consume.service.application;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.global.domain.IContent;
import org.jun.saemangeum.global.domain.Vector;
import org.jun.saemangeum.global.service.VectorService;
import org.jun.saemangeum.pipeline.application.util.VectorCalculator;
import org.jun.saemangeum.pipeline.infrastructure.api.VectorClient;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingResponse;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Component
@RequiredArgsConstructor
public class TableEmbeddingVectorStrategy implements EmbeddingVectorStrategy {

    private final VectorClient vectorClient;
    private final VectorService vectorService;

    @Override
    public List<? extends IContent> calculateSimilarity(String text) {
        EmbeddingResponse response = vectorClient.get(text);
        float[] requestVec = VectorCalculator.addNoise(response.result().embedding());

        List<Vector> vectors = vectorService.getVectors(); // 이거 캐싱 대상이겠는데?
        PriorityQueue<TableEmbeddingVectorStrategy.ContentSimilarity> pq = new PriorityQueue<>();

        for (Vector vec : vectors) {
            float[] storedVec = byteToFloat(vec);
            double similarity = VectorCalculator.cosineSimilarity(requestVec, storedVec);

            TableEmbeddingVectorStrategy.ContentSimilarity cs =
                    new TableEmbeddingVectorStrategy.ContentSimilarity(vec.getContent(), similarity);
            if (pq.size() < 10) {
                pq.offer(cs);
            } else if (similarity > pq.peek().similarity) {
                pq.poll();
                pq.offer(cs);
            }
        }

        return pq.stream().sorted(Comparator.reverseOrder()).map(e -> e.content).toList();
    }

    // 바이트 타입 필드 조회 -> 벡터 플롯 타입 변환
    private float[] byteToFloat(Vector vector) {
        byte[] bytes = vector.getVector();
        FloatBuffer floatBuffer = ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floats = new float[floatBuffer.remaining()];
        floatBuffer.get(floats);
        return floats;
    }

    // 유사도 내부 클래스
    record ContentSimilarity(Content content, double similarity)
            implements Comparable<TableEmbeddingVectorStrategy.ContentSimilarity> {
        @Override
        public int compareTo(TableEmbeddingVectorStrategy.ContentSimilarity o) {
            // 유사도 기준 오름차순 정렬
            return Double.compare(this.similarity, o.similarity);
        }
    }
}
