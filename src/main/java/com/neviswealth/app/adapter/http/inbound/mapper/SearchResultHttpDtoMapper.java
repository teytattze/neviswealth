package com.neviswealth.app.adapter.http.inbound.mapper;

import com.neviswealth.app.adapter.http.inbound.dto.ClientResponse;
import com.neviswealth.app.adapter.http.inbound.dto.DocumentResponse;
import com.neviswealth.app.adapter.http.inbound.dto.SearchResponse;
import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.domain.SearchResult;
import org.springframework.stereotype.Component;

@Component
public class SearchResultHttpDtoMapper implements HttpDtoMapper<SearchResult, SearchResponse> {

    private final HttpDtoMapper<Client, ClientResponse> clientMapper;
    private final HttpDtoMapper<Document, DocumentResponse> documentMapper;

    public SearchResultHttpDtoMapper(
            HttpDtoMapper<Client, ClientResponse> clientMapper,
            HttpDtoMapper<Document, DocumentResponse> documentMapper
    ) {
        this.clientMapper = clientMapper;
        this.documentMapper = documentMapper;
    }

    @Override
    public SearchResponse fromDomain(SearchResult domain) {
        return new SearchResponse(
                domain.clients().stream().map(this.clientMapper::fromDomain).toList(),
                domain.documents().stream().map(this.documentMapper::fromDomain).toList()
        );
    }
}
