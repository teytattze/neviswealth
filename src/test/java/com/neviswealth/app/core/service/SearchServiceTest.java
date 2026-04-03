package com.neviswealth.app.core.service;

import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.port.inbound.SearchUseCasePortInput;
import com.neviswealth.app.core.port.outbound.ClientPersistenceQueryPort;
import com.neviswealth.app.core.port.outbound.DocumentPersistenceQueryPort;
import com.neviswealth.app.core.port.outbound.EmbeddingGeneratorPort;
import com.neviswealth.app.core.port.outbound.LlmSummerizerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private ClientPersistenceQueryPort clientPersistenceQueryPort;

    @Mock
    private DocumentPersistenceQueryPort documentPersistenceQueryPort;

    @Mock
    private EmbeddingGeneratorPort embeddingGeneratorPort;

    @Mock
    private LlmSummerizerPort llmSummerizerPort;

    @InjectMocks
    private SearchService searchService;

    private static final double[] QUERY_EMBEDDING = {0.1, 0.2, 0.3};

    @Test
    void search_withoutSummary_returnsClientsAndDocuments() {
        // given
        var client = Client.create(UUID.randomUUID(), Instant.now(), "John", "Doe", "john@example.com", "desc", List.of());
        var document = Document.create(UUID.randomUUID(), Instant.now(), "Title", "Content", QUERY_EMBEDDING, UUID.randomUUID());

        when(clientPersistenceQueryPort.searchByText("test query")).thenReturn(List.of(client));
        when(embeddingGeneratorPort.generate("test query")).thenReturn(QUERY_EMBEDDING);
        when(documentPersistenceQueryPort.searchByEmbedding(QUERY_EMBEDDING)).thenReturn(List.of(document));

        var input = new SearchUseCasePortInput("test query", false);

        // when
        var result = searchService.search(input);

        // then
        assertThat(result.clients()).containsExactly(client);
        assertThat(result.documents()).containsExactly(document);
        verifyNoInteractions(llmSummerizerPort);
    }

    @Test
    void search_withIncludeSummary_returnsDocumentsWithSummaries() {
        // given
        var document = Document.create(UUID.randomUUID(), Instant.now(), "Title", "Content", QUERY_EMBEDDING, UUID.randomUUID());

        when(clientPersistenceQueryPort.searchByText("test query")).thenReturn(List.of());
        when(embeddingGeneratorPort.generate("test query")).thenReturn(QUERY_EMBEDDING);
        when(documentPersistenceQueryPort.searchByEmbedding(QUERY_EMBEDDING)).thenReturn(List.of(document));
        when(llmSummerizerPort.summarize("Content", "test query")).thenReturn("A summary");

        var input = new SearchUseCasePortInput("test query", true);

        // when
        var result = searchService.search(input);

        // then
        assertThat(result.documents()).hasSize(1);
        assertThat(result.documents().getFirst().summary()).isEqualTo("A summary");
    }

    @Test
    void search_withIncludeSummary_callsSummarizerForEachDocument() {
        // given
        var doc1 = Document.create(UUID.randomUUID(), Instant.now(), "T1", "Content1", QUERY_EMBEDDING, UUID.randomUUID());
        var doc2 = Document.create(UUID.randomUUID(), Instant.now(), "T2", "Content2", QUERY_EMBEDDING, UUID.randomUUID());

        when(clientPersistenceQueryPort.searchByText("query")).thenReturn(List.of());
        when(embeddingGeneratorPort.generate("query")).thenReturn(QUERY_EMBEDDING);
        when(documentPersistenceQueryPort.searchByEmbedding(QUERY_EMBEDDING)).thenReturn(List.of(doc1, doc2));
        when(llmSummerizerPort.summarize(anyString(), eq("query"))).thenReturn("summary");

        var input = new SearchUseCasePortInput("query", true);

        // when
        searchService.search(input);

        // then
        verify(llmSummerizerPort).summarize("Content1", "query");
        verify(llmSummerizerPort).summarize("Content2", "query");
        verifyNoMoreInteractions(llmSummerizerPort);
    }
}
