package org.jun.saemangeum.process.infrastructure.dto;

import java.util.List;

public record ResultDTO(List<Float> embedding, Integer inputTokens) {
}
