package com.neviswealth.app.adapter.persistence.outbound.mapper;

import com.neviswealth.app.adapter.persistence.outbound.model.ClientModel;
import com.neviswealth.app.core.domain.Client;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ClientModelMapperTest {

    private final ClientModelMapper mapper = new ClientModelMapper();

    private static final UUID ID = UUID.randomUUID();
    private static final Instant CREATED_AT = Instant.parse("2026-01-15T10:30:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-01-16T12:00:00Z");
    private static final List<String> SOCIAL_LINKS = List.of("https://linkedin.com/in/johndoe");

    @Test
    void toDomain_validModel_mapsAllFields() {
        // given
        var model = new ClientModel(ID, CREATED_AT, UPDATED_AT, "John", "Doe", "john@example.com", "A client", SOCIAL_LINKS);

        // when
        var domain = mapper.toDomain(model);

        // then
        assertThat(domain.id()).isEqualTo(ID);
        assertThat(domain.createdAt()).isEqualTo(CREATED_AT);
        assertThat(domain.updatedAt()).isEqualTo(UPDATED_AT);
        assertThat(domain.firstName()).isEqualTo("John");
        assertThat(domain.lastName()).isEqualTo("Doe");
        assertThat(domain.email()).isEqualTo("john@example.com");
        assertThat(domain.description()).isEqualTo("A client");
        assertThat(domain.socialLinks()).isEqualTo(SOCIAL_LINKS);
    }

    @Test
    void fromDomain_validDomain_mapsAllFields() {
        // given
        var domain = new Client(ID, CREATED_AT, UPDATED_AT, "John", "Doe", "john@example.com", "A client", SOCIAL_LINKS);

        // when
        var model = mapper.fromDomain(domain);

        // then
        assertThat(model.getId()).isEqualTo(ID);
        assertThat(model.getCreatedAt()).isEqualTo(CREATED_AT);
        assertThat(model.getUpdatedAt()).isEqualTo(UPDATED_AT);
        assertThat(model.getFirstName()).isEqualTo("John");
        assertThat(model.getLastName()).isEqualTo("Doe");
        assertThat(model.getEmail()).isEqualTo("john@example.com");
        assertThat(model.getDescription()).isEqualTo("A client");
        assertThat(model.getSocialLinks()).isEqualTo(SOCIAL_LINKS);
    }
}
