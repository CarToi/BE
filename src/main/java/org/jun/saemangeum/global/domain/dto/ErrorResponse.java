package org.jun.saemangeum.global.domain.dto;

public record ErrorResponse(
        String message,
        String errorMessage
) {
}
