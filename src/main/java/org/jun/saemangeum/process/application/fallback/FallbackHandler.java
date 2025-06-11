package org.jun.saemangeum.process.application.fallback;

import org.jun.saemangeum.global.domain.Content;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FallbackHandler {
    CompletableFuture<List<Content>> handleCollectorFailure(Throwable error);
    CompletableFuture<List<Content>> handlePreProcessorFailure(List<Content> partialContents, Throwable error);
    CompletableFuture<Void> handleSaveFailure(List<Content> contents, Throwable error);
} 