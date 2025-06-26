package org.jun.saemangeum.pipeline.infrastructure.dto;

public record EmbeddingResponse(Status status, Result result) {
    public record Status(String code, String message) {}
    public record Result(float[] embedding) {}
}
