package org.jun.saemangeum.consume.service.strategy;

import org.jun.saemangeum.global.domain.IContent;

import java.util.List;

public interface EmbeddingVectorStrategy {
    List<? extends IContent> calculateSimilarity(String text);
}
