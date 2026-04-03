package com.neviswealth.app.adapter.ai.outbound.mapper;

import com.neviswealth.app.core.exception.CoreExceptionCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class OpenAiExceptionMapperTest {

    private final OpenAiExceptionMapper mapper = new OpenAiExceptionMapper();

    @Test
    void map_rateLimited429_returnsOpenAiRateLimited() {
        // given
        var exception = HttpClientErrorException.create(
                HttpStatusCode.valueOf(429), "Too Many Requests", HttpHeaders.EMPTY, new byte[0], null
        );

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.OPEN_AI_RATE_LIMITED);
        assertThat(result.getMessage()).isEqualTo("AI service rate limit exceeded, please try again later");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_unauthorized401_returnsOpenAiUnauthorized() {
        // given
        var exception = HttpClientErrorException.create(
                HttpStatusCode.valueOf(401), "Unauthorized", HttpHeaders.EMPTY, new byte[0], null
        );

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.OPEN_AI_UNAUTHORIZED);
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_forbidden403_returnsOpenAiUnauthorized() {
        // given
        var exception = HttpClientErrorException.create(
                HttpStatusCode.valueOf(403), "Forbidden", HttpHeaders.EMPTY, new byte[0], null
        );

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.OPEN_AI_UNAUTHORIZED);
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_serverError500_returnsOpenAiServiceFailure() {
        // given
        var exception = HttpServerErrorException.create(
                HttpStatusCode.valueOf(500), "Internal Server Error", HttpHeaders.EMPTY, new byte[0], null
        );

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.OPEN_AI_SERVICE_FAILURE);
        assertThat(result.getMessage()).isEqualTo("AI service returned an error");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_resourceAccessException_returnsOpenAiServiceFailure() {
        // given
        var exception = new ResourceAccessException("Connection refused", new IOException("Connection refused"));

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.OPEN_AI_SERVICE_FAILURE);
        assertThat(result.getMessage()).isEqualTo("Failed to connect to the AI service");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_unknownException_returnsUnknown() {
        // given
        var exception = new RuntimeException("Something unexpected");

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.UNKNOWN);
        assertThat(result.getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(result.getCause()).isEqualTo(exception);
    }
}
