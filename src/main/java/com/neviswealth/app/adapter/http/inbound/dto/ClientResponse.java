package com.neviswealth.app.adapter.http.inbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "Client details")
public record ClientResponse(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String firstName,
        String lastName,
        String email,
        String description,
        List<String> socialLinks
) {
}
