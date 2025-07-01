package org.jun.saemangeum.pipeline.infrastructure.queue;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingJob;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EmbeddingJobQueue {

    private final BlockingQueue<EmbeddingJob> queue;

    public EmbeddingJobQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    // 시간적 텀을 줘서 큐 공간 확보할 대기시간 확보
    public boolean offerQueue(EmbeddingJob job) {
        boolean result = false;

        try {
            result = queue.offer(job, 150, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            log.warn("큐 삽입 실패: {}", job.content().getId());
        }

        return result;
    }

    // 시간적 텀을 줘서 큐 채워질 대기시간 확보
    public EmbeddingJob pollQueue() throws InterruptedException {
        return this.queue.poll(150, TimeUnit.MILLISECONDS);
    }

    // 비었는지 확인
    public boolean isEmptyQueue() {
        return this.queue.isEmpty();
    }

    // 실패 리스트 교체
    public void updateFailedList(List<EmbeddingJob> queue) {
        this.queue.addAll(queue);
    }
}
