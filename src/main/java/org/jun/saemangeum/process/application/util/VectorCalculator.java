package org.jun.saemangeum.process.application.util;

import java.util.Random;

public class VectorCalculator {

    // 노이즈 애더(사용자 요청 벡터에 노이즈 처리)
    public static float[] addNoise(float[] vector) {
        Random random = new Random();
        float randomNoiseLevel = 0.001f + random.nextFloat() * (0.005f - 0.001f);

        float[] noisyVector = new float[vector.length];

        for (int i = 0; i < vector.length; i++) {
            // 노이즈 처리
            noisyVector[i] = vector[i] + (random.nextFloat() * 2 - 1) * randomNoiseLevel;
        }

        return noisyVector;
    }

    // 코사인 유사도 계산
    public static double cosineSimilarity(float[] vecA, float[] vecB) {
        if (vecA.length != vecB.length) {
            throw new IllegalArgumentException("벡터 차원 다름");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.length; i++) {
            dotProduct += vecA[i] * vecB[i];
            normA += vecA[i] * vecA[i];
            normB += vecB[i] * vecB[i];
        }

        if (normA == 0.0 || normB == 0.0) {
            return 0;  // 0으로 나누는 상황 방지
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
