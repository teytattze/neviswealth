package com.neviswealth.app.core.service;

import com.neviswealth.app.core.domain.SearchResult;
import com.neviswealth.app.core.port.inbound.SearchUseCasePort;
import com.neviswealth.app.core.port.inbound.SearchUseCasePortInput;
import com.neviswealth.app.core.port.outbound.ClientPersistenceQueryPort;
import com.neviswealth.app.core.port.outbound.DocumentPersistenceQueryPort;
import com.neviswealth.app.core.port.outbound.EmbeddingGeneratorPort;
import com.neviswealth.app.core.port.outbound.LlmSummerizerPort;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements SearchUseCasePort {

    private final ClientPersistenceQueryPort clientPersistenceQueryPort;
    private final DocumentPersistenceQueryPort documentPersistenceQueryPort;
    private final EmbeddingGeneratorPort embeddingGeneratorPort;
    private final LlmSummerizerPort llmSummerizerPort;

    public SearchService(
            ClientPersistenceQueryPort clientPersistenceQueryPort,
            DocumentPersistenceQueryPort documentPersistenceQueryPort,
            EmbeddingGeneratorPort embeddingGeneratorPort,
            LlmSummerizerPort llmSummerizerPort
    ) {
        this.clientPersistenceQueryPort = clientPersistenceQueryPort;
        this.documentPersistenceQueryPort = documentPersistenceQueryPort;
        this.embeddingGeneratorPort = embeddingGeneratorPort;
        this.llmSummerizerPort = llmSummerizerPort;
    }

    @Override
    public SearchResult search(SearchUseCasePortInput input) {
        var clients = clientPersistenceQueryPort.searchByText(input.query());

        var queryEmbedding = embeddingGeneratorPort.generate(input.query());
        var documents = documentPersistenceQueryPort.searchByEmbedding(queryEmbedding);

        if (input.includeSummary()) {
            documents = documents
                    .stream()
                    .map(document -> document.withSummary(llmSummerizerPort.summarize(document.content(), input.query())))
                    .toList();
        }
        return new SearchResult(clients, documents);
    }
}
