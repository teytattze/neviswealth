package com.neviswealth.app.adapter.persistence.outbound.mapper;

import com.neviswealth.app.adapter.persistence.outbound.model.DocumentModel;
import com.neviswealth.app.core.domain.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Vector;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentModelMapperTest {

    private final DocumentModelMapper mapper = new DocumentModelMapper();

    private static final UUID ID = UUID.randomUUID();
    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final Instant CREATED_AT = Instant.parse("2026-01-15T10:30:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-01-16T12:00:00Z");
    private static final double[] EMBEDDING = {0.1, 0.2, 0.3};

    @Test
    void toDomain_validModel_mapsAllFields() {
        // given
        var model = new DocumentModel(ID, CREATED_AT, UPDATED_AT, "Title", "Content", Vector.of(EMBEDDING), CLIENT_ID);

        // when
        var domain = mapper.toDomain(model);

        // then
        assertThat(domain.id()).isEqualTo(ID);
        assertThat(domain.createdAt()).isEqualTo(CREATED_AT);
        assertThat(domain.updatedAt()).isEqualTo(UPDATED_AT);
        assertThat(domain.title()).isEqualTo("Title");
        assertThat(domain.content()).isEqualTo("Content");
        assertThat(domain.embedding()).isEqualTo(EMBEDDING);
        assertThat(domain.summary()).isNull();
        assertThat(domain.clientId()).isEqualTo(CLIENT_ID);
    }

    @Test
    void fromDomain_validDomain_mapsAllFields() {
        // given
        var domain = new Document(ID, CREATED_AT, UPDATED_AT, "Title", "Content", EMBEDDING, "A summary", CLIENT_ID);

        // when
        var model = mapper.fromDomain(domain);

        // then
        assertThat(model.getId()).isEqualTo(ID);
        assertThat(model.getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(model.getUpdatedAt()).isEqualTo(UPDATED_AT);
        assertThat(model.getTitle()).isEqualTo("Title");
        assertThat(model.getContent()).isEqualTo("Content");
        assertThat(model.getEmbedding().toDoubleArray()).isEqualTo(EMBEDDING);
        assertThat(model.getClientId()).isEqualTo(CLIENT_ID);
    }
}
