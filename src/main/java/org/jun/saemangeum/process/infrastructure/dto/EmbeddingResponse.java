package org.jun.saemangeum.process.infrastructure.dto;

import java.util.List;

public record EmbeddingResponse(Status status, Result result) {
    public record Status(String code, String message) {}
    public record Result(List<Float> embedding) {}
}
