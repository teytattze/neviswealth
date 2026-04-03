package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.core.exception.CoreException;
import com.neviswealth.app.core.exception.CoreExceptionCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleCoreException_unknownCode_returns500() {
        // given
        var exception = new CoreException(CoreExceptionCode.UNKNOWN, "Something went wrong", null);

        // when
        var response = handler.handleCoreException(exception);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().message()).isEqualTo("Something went wrong");
    }

    @Test
    void handleCoreException_mongoDuplicateKey_returns409() {
        // given
        var exception = new CoreException(CoreExceptionCode.MONGO_DUPLICATE_KEY, "Duplicate key", null);

        // when
        var response = handler.handleCoreException(exception);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(409);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().message()).isEqualTo("Duplicate key");
    }

    @Test
    void handleCoreException_mongoDataAccessFailure_returns502() {
        // given
        var exception = new CoreException(CoreExceptionCode.MONGO_DATA_ACCESS_FAILURE, "Database error", null);

        // when
        var response = handler.handleCoreException(exception);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(502);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(502);
        assertThat(response.getBody().message()).isEqualTo("Database error");
    }

    @Test
    void handleCoreException_openAiServiceFailure_returns502() {
        // given
        var exception = new CoreException(CoreExceptionCode.OPEN_AI_SERVICE_FAILURE, "AI service error", null);

        // when
        var response = handler.handleCoreException(exception);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(502);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(502);
        assertThat(response.getBody().message()).isEqualTo("AI service error");
    }

    @Test
    void handleCoreException_openAiRateLimited_returns503() {
        // given
        var exception = new CoreException(CoreExceptionCode.OPEN_AI_RATE_LIMITED, "Rate limited", null);

        // when
        var response = handler.handleCoreException(exception);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(503);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(503);
        assertThat(response.getBody().message()).isEqualTo("Rate limited");
    }

    @Test
    void handleCoreException_openAiUnauthorized_returns502() {
        // given
        var exception = new CoreException(CoreExceptionCode.OPEN_AI_UNAUTHORIZED, "Unauthorized", null);

        // when
        var response = handler.handleCoreException(exception);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(502);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(502);
        assertThat(response.getBody().message()).isEqualTo("Unauthorized");
    }

    @Test
    void handleUnexpected_anyException_returns500() {
        // given
        var exception = new RuntimeException("Unexpected error");

        // when
        var response = handler.handleUnexpected(exception);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().message()).isEqualTo("Internal server error");
    }
}
