package com.neviswealth.app.adapter.ai.outbound.mapper;

import com.neviswealth.app.core.exception.CoreException;
import com.neviswealth.app.core.exception.CoreExceptionCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class OpenAiExceptionMapper implements ExceptionMapper {

    @Override
    public CoreException map(Exception exception) {
        if (exception instanceof RestClientResponseException responseException) {
            return mapResponseException(responseException);
        }
        if (exception instanceof ResourceAccessException) {
            return new CoreException(
                    CoreExceptionCode.OPEN_AI_SERVICE_FAILURE,
                    "Failed to connect to the AI service",
                    exception
            );
        }
        return new CoreException(
                CoreExceptionCode.UNKNOWN,
                "An unexpected error occurred",
                exception
        );
    }

    private CoreException mapResponseException(RestClientResponseException exception) {
        int statusCode = exception.getStatusCode().value();
        if (statusCode == 429) {
            return new CoreException(
                    CoreExceptionCode.OPEN_AI_RATE_LIMITED,
                    "AI service rate limit exceeded, please try again later",
                    exception
            );
        }
        if (statusCode == 401 || statusCode == 403) {
            return new CoreException(
                    CoreExceptionCode.OPEN_AI_UNAUTHORIZED,
                    "AI service authentication failed",
                    exception
            );
        }
        return new CoreException(
                CoreExceptionCode.OPEN_AI_SERVICE_FAILURE,
                "AI service returned an error",
                exception
        );
    }
}
