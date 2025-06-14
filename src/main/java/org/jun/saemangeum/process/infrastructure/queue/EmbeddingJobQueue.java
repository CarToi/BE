package org.jun.saemangeum.process.infrastructure.queue;

import org.jun.saemangeum.process.infrastructure.dto.EmbeddingJob;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class EmbeddingJobQueue {

    private final BlockingQueue<EmbeddingJob> queue;

    public EmbeddingJobQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }
}
