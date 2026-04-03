package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.ErrorResponse;
import com.neviswealth.app.core.error.CoreException;
import com.neviswealth.app.core.error.CoreExceptionCode;
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
