package com.neviswealth.app.adapter.http.inbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Document details")
public record DocumentResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String content,
        String summary,
        UUID clientId
) {
}
