package com.neviswealth.app.adapter.ai.outbound.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChatRole {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String value;

    ChatRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
