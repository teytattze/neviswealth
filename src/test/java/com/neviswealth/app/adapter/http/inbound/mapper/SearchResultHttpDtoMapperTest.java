package com.neviswealth.app.adapter.http.inbound.mapper;

import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.domain.SearchResult;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SearchResultHttpDtoMapperTest {

    private final ClientHttpDtoMapper clientMapper = new ClientHttpDtoMapper();
    private final DocumentHttpDtoMapper documentMapper = new DocumentHttpDtoMapper();
    private final SearchResultHttpDtoMapper mapper = new SearchResultHttpDtoMapper(clientMapper, documentMapper);

    @Test
    void fromDomain_validSearchResult_mapsClientsAndDocuments() {
        // given
        var client = Client.create(UUID.randomUUID(), Instant.now(), "John", "Doe", "john@example.com", "desc", List.of());
        var document = Document.create(UUID.randomUUID(), Instant.now(), "Title", "Content", new double[]{0.1}, UUID.randomUUID());
        var searchResult = new SearchResult(List.of(client), List.of(document));

        // when
        var response = mapper.fromDomain(searchResult);

        // then
        assertThat(response.clients()).hasSize(1);
        assertThat(response.clients().getFirst().firstName()).isEqualTo("John");
        assertThat(response.documents()).hasSize(1);
        assertThat(response.documents().getFirst().title()).isEqualTo("Title");
    }

    @Test
    void fromDomain_emptySearchResult_mapsEmptyLists() {
        // given
        var searchResult = new SearchResult(List.of(), List.of());

        // when
        var response = mapper.fromDomain(searchResult);

        // then
        assertThat(response.clients()).isEmpty();
        assertThat(response.documents()).isEmpty();
    }
}
