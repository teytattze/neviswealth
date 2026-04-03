package com.neviswealth.app.core.domain;

import java.util.List;

public record SearchResult(
        List<Client> clients,
        List<Document> documents
) {
}
