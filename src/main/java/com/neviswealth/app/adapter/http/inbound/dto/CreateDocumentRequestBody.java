package com.neviswealth.app.adapter.http.inbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for creating a new document")
public record CreateDocumentRequestBody(
        @NotBlank
        String title,
        @NotBlank
        String content
) {
}
