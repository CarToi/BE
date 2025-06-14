package org.jun.saemangeum.process.infrastructure.queue;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.process.application.embed.EmbeddingVectorService;
import org.jun.saemangeum.process.infrastructure.dto.EmbeddingJob;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EmbeddingWorker implements Runnable {

    private volatile boolean running = true;
    private final EmbeddingJobQueue queue;
    private final EmbeddingVectorService service;

    // 실패시 수집용 리스트
    private final List<Content> failedContents = Collections.synchronizedList(new ArrayList<>());

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
                EmbeddingJob job = queue.pollQueue();
                if (job != null) {
                    boolean success = false;
                    int attempts = 0;

                    while (!success && attempts < 3) {
                        try {
                            service.embeddingVector(job.content());
                            success = true;
                            Thread.sleep(500); // 호출 속도 조절
                        } catch (HttpClientErrorException.TooManyRequests e) {
                            // 0.5초 -> 1초 -> 2초
                            long delay = (long) Math.pow(2, attempts) * 500;
                            log.warn("429 호출 속도 과다: {}", job.content().getId());
                            Thread.sleep(delay);
                            attempts++;
                        } catch (HttpClientErrorException.BadRequest e) {
                            log.error("토큰 길이 초과: {}", job.content().getId());
                        }
                    }

                    if (!success) {
                        // 재시도 큐를 구축하자
                        log.error("최대 재시도 실패, 해당 컨텐츠는 벡터 임베딩 생략: {}", job.content().getId());
                        failedContents.add(job.content());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("스레드 인터럽팅");
                break;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public List<Content> getFailedContents() {
        return new ArrayList<>(failedContents);
    }
}
