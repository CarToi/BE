package org.jun.saemangeum.process.infrastructure.queue;

import lombok.RequiredArgsConstructor;
import org.jun.saemangeum.process.application.embed.EmbeddingVectorService;
import org.springframework.stereotype.Service;

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

    public void stopWorker() {
        if (worker != null) {
            worker.stop();
        }
    }
}
