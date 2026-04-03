package com.neviswealth.app.core.error;

public class CoreException extends RuntimeException {
    private final CoreExceptionCode code;

    public CoreException(CoreExceptionCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public CoreExceptionCode getCode() {
        return code;
    }
}
