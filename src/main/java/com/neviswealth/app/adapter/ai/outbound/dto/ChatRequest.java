package com.neviswealth.app.adapter.ai.outbound.dto;

import java.util.List;

public record ChatRequest(OpenAiModel model, List<ChatMessageRequest> messages) {
}
