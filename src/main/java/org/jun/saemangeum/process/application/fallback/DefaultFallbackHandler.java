package org.jun.saemangeum.process.application.fallback;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.domain.Content;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class DefaultFallbackHandler implements FallbackHandler {
    
    @Override
    public CompletableFuture<List<Content>> handleCollectorFailure(Throwable error) {
        log.error("컨텐츠 수집 실패", error);
        // 빈 리스트 반환 - 다음 수집 시도를 위해 프로세스는 계속 진행
        return CompletableFuture.completedFuture(new ArrayList<>());
    }

    @Override
    public CompletableFuture<List<Content>> handlePreProcessorFailure(List<Content> partialContents, Throwable error) {
        log.error("전처리 실패 - 부분 처리된 컨텐츠: {}", partialContents.size(), error);
        // 부분적으로 처리된 컨텐츠라도 저장
        return CompletableFuture.completedFuture(partialContents);
    }

    @Override
    public CompletableFuture<Void> handleSaveFailure(List<Content> contents, Throwable error) {
        log.error("컨텐츠 저장 실패 - 저장 실패한 컨텐츠: {}", contents.size(), error);
        // 임시 저장소에 백업하거나 다음 배치 작업을 위해 큐에 넣는 등의 복구 로직을 구현할 수 있음
        return CompletableFuture.completedFuture(null);
    }
} 