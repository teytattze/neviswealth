package com.neviswealth.app.adapter.persistence.outbound.mapper;

import com.neviswealth.app.adapter.persistence.outbound.model.DocumentModel;
import com.neviswealth.app.core.domain.Document;
import org.springframework.data.domain.Vector;
import org.springframework.stereotype.Component;

@Component
public class DocumentModelMapper implements ModelMapper<Document, DocumentModel> {
    public Document toDomain(DocumentModel model) {
        return new Document(
                model.getId(),
                model.getCreatedAt(),
                model.getUpdatedAt(),

                model.getTitle(),
                model.getContent(),
                model.getEmbedding().toDoubleArray(),
                null,

                model.getClientId()
        );
    }

    public DocumentModel fromDomain(Document domain) {
        return new DocumentModel(
                domain.id(),
                domain.createdAt(),
                domain.updatedAt(),

                domain.title(),
                domain.content(),
                Vector.of(domain.embedding()),

                domain.clientId()
        );
    }
}
