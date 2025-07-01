package org.jun.saemangeum.global.config;

import lombok.extern.slf4j.Slf4j;
import org.jun.saemangeum.global.exception.ClientIdException;
import org.jun.saemangeum.global.exception.ErrorResponse;
import org.jun.saemangeum.global.exception.SatisfactionsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SatisfactionsException.class)
    public ResponseEntity<ErrorResponse> handleSatisfactionsException(SatisfactionsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorResponse(e.getMessage(), e.getLocalizedMessage()));
    }

    @ExceptionHandler(ClientIdException.class)
    public ResponseEntity<ErrorResponse> handleClientIdException(ClientIdException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorResponse(e.getMessage(), e.getLocalizedMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS.value())
                .body(new ErrorResponse("현재 요청이 과다하게 몰리고 있습니다. 잠시 후에 시도해주세요.", e.getMessage()));
    }
}
