package org.jun.saemangeum.global.exception;

public record ErrorResponse(
        String message,
        String errorMessage
) {
}
