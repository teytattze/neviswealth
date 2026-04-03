package com.neviswealth.app.core.error;

public enum CoreExceptionCode {
    UNKNOWN("00000");

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
