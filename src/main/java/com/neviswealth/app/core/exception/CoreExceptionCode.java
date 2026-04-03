package com.neviswealth.app.core.exception;

public enum CoreExceptionCode {
    UNKNOWN("00000"),
    MONGO_DUPLICATE_KEY("10001"),
    MONGO_DATA_ACCESS_FAILURE("10002"),
    OPEN_AI_SERVICE_FAILURE("20001"),
    OPEN_AI_RATE_LIMITED("20002"),
    OPEN_AI_UNAUTHORIZED("20003"),
    CLIENT_NOT_FOUND("30001");

    private final String code;

    CoreExceptionCode(String code) {
        this.code = code;
    }

    public static CoreExceptionCode valueOfCode(String code) {
        for (CoreExceptionCode e : values()) {
            if (java.util.Objects.equals(e.getCode(), code)) {
                return e;
            }
        }
        return CoreExceptionCode.UNKNOWN;
    }

    public String getCode() {
        return code;
    }
}
