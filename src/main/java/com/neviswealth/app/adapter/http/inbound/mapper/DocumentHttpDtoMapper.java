package com.neviswealth.app.adapter.http.inbound.mapper;

import com.neviswealth.app.adapter.http.inbound.dto.DocumentResponse;
import com.neviswealth.app.core.domain.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentHttpDtoMapper implements HttpDtoMapper<Document, DocumentResponse> {

    @Override
    public DocumentResponse fromDomain(Document domain) {
        return new DocumentResponse(
                domain.id(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.title(),
                domain.content(),
                domain.summary(),
                domain.clientId()
        );
    }
}
