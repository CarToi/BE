package org.jun.saemangeum.process.infrastructure.queue;

import org.jun.saemangeum.process.infrastructure.dto.EmbeddingJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class EmbeddingJobQueue {

    private final BlockingQueue<EmbeddingJob> queue;

    public EmbeddingJobQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    // 시간적 텀을 줘서 큐 공간 확보할 대기시간 확보
    public boolean offer(EmbeddingJob job) throws InterruptedException {
        return this.queue.offer(job, 150, TimeUnit.MILLISECONDS);
    }

    // 시간적 텀을 줘서 큐 채워질 대기시간 확보
    public EmbeddingJob poll() throws InterruptedException {
        return this.queue.poll(150, TimeUnit.MILLISECONDS);
    }

    // 비었는지 확인
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
