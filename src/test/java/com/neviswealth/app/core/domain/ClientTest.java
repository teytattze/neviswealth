package com.neviswealth.app.core.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ClientTest {

    @Test
    void create_validInput_setsCreatedAtAndUpdatedAtToNow() {
        // given
        var id = UUID.randomUUID();
        var now = Instant.parse("2026-01-15T10:30:00Z");

        // when
        var client = Client.create(id, now, "John", "Doe", "john@example.com", "A client", List.of());

        // then
        assertThat(client.createdAt()).isEqualTo(now);
        assertThat(client.updatedAt()).isEqualTo(now);
    }

    @Test
    void create_validInput_setsAllFields() {
        // given
        var id = UUID.randomUUID();
        var now = Instant.now();
        var socialLinks = List.of("https://linkedin.com/in/johndoe");

        // when
        var client = Client.create(id, now, "John", "Doe", "john@example.com", "A client", socialLinks);

        // then
        assertThat(client.id()).isEqualTo(id);
        assertThat(client.firstName()).isEqualTo("John");
        assertThat(client.lastName()).isEqualTo("Doe");
        assertThat(client.email()).isEqualTo("john@example.com");
        assertThat(client.description()).isEqualTo("A client");
        assertThat(client.socialLinks()).containsExactly("https://linkedin.com/in/johndoe");
    }
}
