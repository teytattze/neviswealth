package com.neviswealth.app.adapter.ai.outbound.dto;

import java.util.List;

public record ChatResponse(List<ChatChoiceResponse> choices) {
}
