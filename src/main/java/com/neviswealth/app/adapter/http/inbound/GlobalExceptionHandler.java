package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.ErrorResponse;
import com.neviswealth.app.core.exception.CoreException;
import com.neviswealth.app.core.exception.CoreExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ErrorResponse.of(HttpStatus.UNPROCESSABLE_CONTENT.value(), message));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"));
    }

    @ExceptionHandler(CoreException.class)
    ResponseEntity<ErrorResponse> handleCoreException(CoreException ex) {
        var httpStatus = this.toHttpStatus(ex.getCode());
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.of(httpStatus.value(), ex.getMessage()));
    }

    private HttpStatus toHttpStatus(CoreExceptionCode code) {
        return switch (code) {
            case UNKNOWN -> HttpStatus.INTERNAL_SERVER_ERROR;
            case MONGO_DUPLICATE_KEY -> HttpStatus.CONFLICT;
            case MONGO_DATA_ACCESS_FAILURE,
                 OPEN_AI_SERVICE_FAILURE,
                 OPEN_AI_UNAUTHORIZED -> HttpStatus.BAD_GATEWAY;
            case OPEN_AI_RATE_LIMITED -> HttpStatus.SERVICE_UNAVAILABLE;
        };
    }
}
