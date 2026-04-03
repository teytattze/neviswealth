package com.neviswealth.app.core.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentTest {

    private static final UUID ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final Instant NOW = Instant.parse("2026-01-15T10:30:00Z");
    private static final double[] EMBEDDING = {0.1, 0.2, 0.3};

    @Test
    void create_validInput_setsSummaryToNull() {
        // given / when
        var document = Document.create(ID, NOW, "Title", "Content", EMBEDDING, CLIENT_ID);

        // then
        assertThat(document.summary()).isNull();
    }

    @Test
    void create_validInput_setsAllFields() {
        // given / when
        var document = Document.create(ID, NOW, "Title", "Content", EMBEDDING, CLIENT_ID);

        // then
        assertThat(document.id()).isEqualTo(ID);
        assertThat(document.createdAt()).isEqualTo(NOW);
        assertThat(document.updatedAt()).isEqualTo(NOW);
        assertThat(document.title()).isEqualTo("Title");
        assertThat(document.content()).isEqualTo("Content");
        assertThat(document.embedding()).isEqualTo(EMBEDDING);
        assertThat(document.clientId()).isEqualTo(CLIENT_ID);
    }

    @Test
    void withSummary_validSummary_returnsNewDocumentWithSummary() {
        // given
        var document = Document.create(ID, NOW, "Title", "Content", EMBEDDING, CLIENT_ID);

        // when
        var updated = document.withSummary("A summary");

        // then
        assertThat(updated.summary()).isEqualTo("A summary");
        assertThat(updated.id()).isEqualTo(document.id());
        assertThat(updated.createdAt()).isEqualTo(document.createdAt());
        assertThat(updated.updatedAt()).isEqualTo(document.updatedAt());
        assertThat(updated.title()).isEqualTo(document.title());
        assertThat(updated.content()).isEqualTo(document.content());
        assertThat(updated.embedding()).isEqualTo(document.embedding());
        assertThat(updated.clientId()).isEqualTo(document.clientId());
    }
}
