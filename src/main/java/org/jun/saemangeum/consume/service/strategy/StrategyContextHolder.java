package org.jun.saemangeum.consume.service.strategy;

public class StrategyContextHolder {
//    private static final ThreadLocal<EmbeddingVectorStrategy> strategyHolder = new ThreadLocal<>();
//
//    public static void setStrategy(EmbeddingVectorStrategy strategy) {
//        strategyHolder.set(strategy);
//    }
//
//    public static EmbeddingVectorStrategy getStrategy() {
//        return strategyHolder.get();
//    }
//
//    public static void clear() {
//        strategyHolder.remove();
//    }
    private static EmbeddingVectorStrategy currentStrategy;

    public static void setStrategy(EmbeddingVectorStrategy strategy) {
        currentStrategy = strategy;
    }

    public static EmbeddingVectorStrategy getStrategy() {
        return currentStrategy;
    }
}
