package com.neviswealth.app.core.service;

import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.port.inbound.CreateDocumentUseCasePort;
import com.neviswealth.app.core.port.inbound.CreateDocumentUseCasePortInput;
import com.neviswealth.app.core.port.outbound.ClockPort;
import com.neviswealth.app.core.port.outbound.DocumentPersistenceSavePort;
import com.neviswealth.app.core.port.outbound.EmbeddingGeneratorPort;
import com.neviswealth.app.core.port.outbound.UuidGeneratorPort;
import org.springframework.stereotype.Service;

@Service
public class DocumentService implements CreateDocumentUseCasePort {

    private final DocumentPersistenceSavePort documentPersistenceSavePort;
    private final EmbeddingGeneratorPort embeddingGeneratorPort;
    private final UuidGeneratorPort uuidGeneratorPort;
    private final ClockPort clockPort;

    public DocumentService(
            DocumentPersistenceSavePort documentPersistenceSavePort,
            EmbeddingGeneratorPort embeddingGeneratorPort,
            UuidGeneratorPort uuidGeneratorPort,
            ClockPort clockPort
    ) {
        this.documentPersistenceSavePort = documentPersistenceSavePort;
        this.embeddingGeneratorPort = embeddingGeneratorPort;
        this.uuidGeneratorPort = uuidGeneratorPort;
        this.clockPort = clockPort;
    }

    @Override
    public Document createDocument(CreateDocumentUseCasePortInput input) {
        var embedding = embeddingGeneratorPort.generate(input.title() + " " + input.content());
        var document = Document.create(
                uuidGeneratorPort.generate(),
                clockPort.now(),
                input.title(),
                input.content(),
                embedding,
                input.clientId()
        );
        this.documentPersistenceSavePort.save(document);
        return document;
    }
}
