package com.neviswealth.app.adapter.ai.outbound.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OpenAiModel {
    GPT_4O_MINI("gpt-4o-mini"),
    GPT_5_4_MINI("gpt-5.4-mini"),
    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large");

    private final String value;

    OpenAiModel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OpenAiModel fromValue(String value) {
        for (OpenAiModel model : values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Unknown OpenAI model: " + value);
    }
}
