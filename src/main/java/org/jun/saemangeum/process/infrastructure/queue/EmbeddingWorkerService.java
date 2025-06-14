package org.jun.saemangeum.process.infrastructure.queue;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.process.application.embed.EmbeddingVectorService;
import org.jun.saemangeum.process.infrastructure.dto.EmbeddingJob;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingWorkerService {

    private final EmbeddingVectorService embeddingVectorService;
    private final EmbeddingJobQueue embeddingJobQueue;

    private Thread workerThread;
    private EmbeddingWorker worker;

    public void startWorker() {
        if (workerThread == null || !workerThread.isAlive()) {
            worker = new EmbeddingWorker(embeddingJobQueue, embeddingVectorService);
            workerThread = Thread.ofVirtual().start(worker);
        }
    }

    public void offerEmbeddingJobQueue(EmbeddingJob job) {
        embeddingJobQueue.offerQueue(job);
    }

    public boolean isEmpty() {
        return embeddingJobQueue.isEmptyQueue();
    }

    public void stopWorker() {
        if (worker != null) {
            worker.stop();
        }
    }

    public List<Content> getFailedContents() {
        return worker != null ? worker.getFailedContents() : List.of();
    }
}
