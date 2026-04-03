package com.neviswealth.app.adapter.http.inbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "Request body for creating a new client")
public record CreateClientRequestBody(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Email
        @Schema(example = "john.doe@example.com")
        String email,
        String description,
        @Schema(description = "List of social media profile URLs", example = "[\"https://linkedin.com/in/johndoe\"]")
        List<String> socialLinks
) {
}
