package com.neviswealth.app.adapter.http.inbound.mapper;

import com.neviswealth.app.core.domain.Client;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ClientHttpDtoMapperTest {

    private final ClientHttpDtoMapper mapper = new ClientHttpDtoMapper();

    @Test
    void fromDomain_validClient_mapsAllFields() {
        // given
        var id = UUID.randomUUID();
        var createdAt = Instant.parse("2026-01-15T10:30:00Z");
        var updatedAt = Instant.parse("2026-01-16T12:00:00Z");
        var socialLinks = List.of("https://linkedin.com/in/johndoe");
        var client = new Client(id, createdAt, updatedAt, "John", "Doe", "john@example.com", "A client", socialLinks);

        // when
        var response = mapper.fromDomain(client);

        // then
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
        assertThat(response.firstName()).isEqualTo("John");
        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.description()).isEqualTo("A client");
        assertThat(response.socialLinks()).containsExactly("https://linkedin.com/in/johndoe");
    }
}
