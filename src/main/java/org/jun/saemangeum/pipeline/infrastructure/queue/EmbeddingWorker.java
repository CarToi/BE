package org.jun.saemangeum.pipeline.infrastructure.queue;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.jun.saemangeum.pipeline.application.service.EmbeddingVectorService;
import org.jun.saemangeum.pipeline.infrastructure.dto.EmbeddingJob;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EmbeddingWorker implements Runnable {

    private final EmbeddingJobQueue queue;
    private final EmbeddingVectorService service;

    // 실패시 수집용 리스트
    private final List<EmbeddingJob> failedContents = Collections.synchronizedList(new ArrayList<>());

    public EmbeddingWorker(EmbeddingJobQueue queue, EmbeddingVectorService service) {
        this.queue = queue;
        this.service = service;
    }

    @Override
    public void run() {
        while (true) {
            try {
                EmbeddingJob job = queue.pollQueue();
                if (job == null) break;

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
                    log.error("최대 재시도 실패, 해당 컨텐츠는 일단 벡터 임베딩 생략: {}", job.content().getId());
                    failedContents.add(job);
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

    public List<EmbeddingJob> getFailedContents() {
        return new ArrayList<>(failedContents);
    }
}
