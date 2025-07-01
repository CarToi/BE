package org.jun.saemangeum.consume.service.strategy;

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

    public static EmbeddingVectorStrategy getStrategy() {
        lock.readLock().lock();
        try {
            return currentStrategy;
        } finally {
            lock.readLock().unlock();
        }
    }
}
