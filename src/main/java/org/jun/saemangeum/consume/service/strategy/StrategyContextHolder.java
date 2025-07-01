package org.jun.saemangeum.consume.service.strategy;

import org.jun.saemangeum.global.domain.IContent;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StrategyContextHolder {
    private static EmbeddingVectorStrategy currentStrategy;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void setStrategy(EmbeddingVectorStrategy strategy) {
        lock.writeLock().lock();
        try {
            currentStrategy = strategy;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static List<? extends IContent> executeStrategy(String text) {
        lock.readLock().lock();
        try {
            return currentStrategy.calculateSimilarity(text);
        } finally {
            lock.readLock().unlock();
        }
    }
}
