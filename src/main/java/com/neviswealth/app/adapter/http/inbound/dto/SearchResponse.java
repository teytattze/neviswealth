package com.neviswealth.app.adapter.http.inbound.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Search results across clients and documents")
public record SearchResponse(
        List<ClientResponse> clients,
        List<DocumentResponse> documents
) {
}
