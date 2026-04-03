package com.neviswealth.app.core.service;

import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.port.inbound.CreateDocumentUseCasePortInput;
import com.neviswealth.app.core.port.outbound.ClockPort;
import com.neviswealth.app.core.port.outbound.DocumentPersistenceSavePort;
import com.neviswealth.app.core.port.outbound.EmbeddingGeneratorPort;
import com.neviswealth.app.core.port.outbound.UuidGeneratorPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentPersistenceSavePort documentPersistenceSavePort;

    @Mock
    private EmbeddingGeneratorPort embeddingGeneratorPort;

    @Mock
    private UuidGeneratorPort uuidGeneratorPort;

    @Mock
    private ClockPort clockPort;

    @InjectMocks
    private DocumentService documentService;

    private static final UUID GENERATED_ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final Instant NOW = Instant.parse("2026-01-15T10:30:00Z");
    private static final double[] EMBEDDING = {0.1, 0.2, 0.3};

    @Test
    void createDocument_validInput_generatesEmbeddingFromTitleAndContent() {
        // given
        when(uuidGeneratorPort.generate()).thenReturn(GENERATED_ID);
        when(clockPort.now()).thenReturn(NOW);
        when(embeddingGeneratorPort.generate("My Title My Content")).thenReturn(EMBEDDING);

        var input = new CreateDocumentUseCasePortInput("My Title", "My Content", CLIENT_ID);

        // when
        documentService.createDocument(input);

        // then
        verify(embeddingGeneratorPort).generate("My Title My Content");
    }

    @Test
    void createDocument_validInput_returnsCreatedDocument() {
        // given
        when(uuidGeneratorPort.generate()).thenReturn(GENERATED_ID);
        when(clockPort.now()).thenReturn(NOW);
        when(embeddingGeneratorPort.generate("Title Content")).thenReturn(EMBEDDING);

        var input = new CreateDocumentUseCasePortInput("Title", "Content", CLIENT_ID);

        // when
        var result = documentService.createDocument(input);

        // then
        assertThat(result.id()).isEqualTo(GENERATED_ID);
        assertThat(result.createdAt()).isEqualTo(NOW);
        assertThat(result.updatedAt()).isEqualTo(NOW);
        assertThat(result.title()).isEqualTo("Title");
        assertThat(result.content()).isEqualTo("Content");
        assertThat(result.embedding()).isEqualTo(EMBEDDING);
        assertThat(result.summary()).isNull();
        assertThat(result.clientId()).isEqualTo(CLIENT_ID);
    }

    @Test
    void createDocument_validInput_savesDocumentViaPersistencePort() {
        // given
        when(uuidGeneratorPort.generate()).thenReturn(GENERATED_ID);
        when(clockPort.now()).thenReturn(NOW);
        when(embeddingGeneratorPort.generate("Title Content")).thenReturn(EMBEDDING);

        var input = new CreateDocumentUseCasePortInput("Title", "Content", CLIENT_ID);

        // when
        documentService.createDocument(input);

        // then
        var captor = ArgumentCaptor.forClass(Document.class);
        verify(documentPersistenceSavePort).save(captor.capture());

        var savedDocument = captor.getValue();
        assertThat(savedDocument.id()).isEqualTo(GENERATED_ID);
        assertThat(savedDocument.title()).isEqualTo("Title");
        assertThat(savedDocument.clientId()).isEqualTo(CLIENT_ID);
    }
}
