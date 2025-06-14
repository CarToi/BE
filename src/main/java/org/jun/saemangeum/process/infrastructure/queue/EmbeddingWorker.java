package org.jun.saemangeum.process.infrastructure.queue;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.process.application.embed.EmbeddingVectorService;
import org.jun.saemangeum.process.infrastructure.dto.EmbeddingJob;

@Slf4j
public class EmbeddingWorker implements Runnable {

    private volatile boolean running = true;
    private final EmbeddingJobQueue queue;
    private final EmbeddingVectorService service;

    public EmbeddingWorker(EmbeddingJobQueue queue, EmbeddingVectorService service) {
        this.queue = queue;
        this.service = service;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                EmbeddingJob job = queue.poll();
                if (job != null) {
                    service.embeddingVector(job.content());
                    log.info("임베딩 벡터 성공: {}", job.content().getId());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
