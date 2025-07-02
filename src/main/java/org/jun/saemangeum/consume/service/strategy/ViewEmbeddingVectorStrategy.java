package org.jun.saemangeum.consume.service.strategy;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.consume.domain.swap.ContentView;
import org.jun.saemangeum.consume.domain.swap.VectorView;
import org.jun.saemangeum.consume.service.swap.SwapViewService;
import org.jun.saemangeum.global.domain.IContent;
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
public class ViewEmbeddingVectorStrategy implements EmbeddingVectorStrategy {

    private final VectorClient vectorClient;
    private final SwapViewService swapViewService;

    @Override
    public List<? extends IContent> calculateSimilarity(String text) {
        EmbeddingResponse response = vectorClient.get(text);
        float[] requestVec = VectorCalculator.addNoise(response.result().embedding());

        List<VectorView> vectorViews = swapViewService.getVectorViews(); // 이거 캐싱 대상이겠는데?
        PriorityQueue<ViewEmbeddingVectorStrategy.ContentSimilarity> pq = new PriorityQueue<>();

        for (VectorView vec : vectorViews) {
            float[] storedVec = byteToFloat(vec);
            double similarity = VectorCalculator.cosineSimilarity(requestVec, storedVec);

            ViewEmbeddingVectorStrategy.ContentSimilarity cs =
                    new ViewEmbeddingVectorStrategy.ContentSimilarity(vec.getContentView(), similarity);
            if (pq.size() < 18) {
                pq.offer(cs);
            } else if (similarity > pq.peek().similarity) {
                pq.poll();
                pq.offer(cs);
            }
        }

        return pq.stream().sorted(Comparator.reverseOrder()).map(e -> e.contentView).toList();
    }

    // 바이트 타입 필드 조회 -> 벡터 플롯 타입 변환
    private float[] byteToFloat(VectorView vectorView) {
        byte[] bytes = vectorView.getVector();
        FloatBuffer floatBuffer = ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floats = new float[floatBuffer.remaining()];
        floatBuffer.get(floats);
        return floats;
    }

    // 유사도 내부 클래스
    record ContentSimilarity(ContentView contentView, double similarity)
            implements Comparable<ViewEmbeddingVectorStrategy.ContentSimilarity> {
        @Override
        public int compareTo(ViewEmbeddingVectorStrategy.ContentSimilarity o) {
            // 유사도 기준 오름차순 정렬
            return Double.compare(this.similarity, o.similarity);
        }
    }
}
