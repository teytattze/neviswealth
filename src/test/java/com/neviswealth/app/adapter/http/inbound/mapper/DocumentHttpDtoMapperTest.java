package com.neviswealth.app.adapter.http.inbound.mapper;

import com.neviswealth.app.core.domain.Document;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentHttpDtoMapperTest {

    private final DocumentHttpDtoMapper mapper = new DocumentHttpDtoMapper();

    @Test
    void fromDomain_validDocument_mapsAllFields() {
        // given
        var id = UUID.randomUUID();
        var clientId = UUID.randomUUID();
        var createdAt = Instant.parse("2026-01-15T10:30:00Z");
        var updatedAt = Instant.parse("2026-01-16T12:00:00Z");
        var document = new Document(id, createdAt, updatedAt, "Title", "Content", new double[]{0.1}, "A summary", clientId);

        // when
        var response = mapper.fromDomain(document);

        // then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
        assertThat(response.title()).isEqualTo("Title");
        assertThat(response.content()).isEqualTo("Content");
        assertThat(response.summary()).isEqualTo("A summary");
        assertThat(response.clientId()).isEqualTo(clientId);
    }

    @Test
    void fromDomain_documentWithNullSummary_mapsNullSummary() {
        // given
        var document = Document.create(UUID.randomUUID(), Instant.now(), "Title", "Content", new double[]{0.1}, UUID.randomUUID());

        // when
        var response = mapper.fromDomain(document);

        // then
        assertThat(response.summary()).isNull();
    }
}
