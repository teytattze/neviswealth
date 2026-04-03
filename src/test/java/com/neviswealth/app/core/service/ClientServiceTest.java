package com.neviswealth.app.core.service;

import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePortInput;
import com.neviswealth.app.core.port.outbound.ClientPersistenceSavePort;
import com.neviswealth.app.core.port.outbound.ClockPort;
import com.neviswealth.app.core.port.outbound.UuidGeneratorPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientPersistenceSavePort clientPersistenceSavePort;

    @Mock
    private UuidGeneratorPort uuidGeneratorPort;

    @Mock
    private ClockPort clockPort;

    @InjectMocks
    private ClientService clientService;

    private static final UUID GENERATED_ID = UUID.randomUUID();
    private static final Instant NOW = Instant.parse("2026-01-15T10:30:00Z");

    @Test
    void createClient_validInput_returnsCreatedClient() {
        // given
        when(uuidGeneratorPort.generate()).thenReturn(GENERATED_ID);
        when(clockPort.now()).thenReturn(NOW);

        var input = new CreateClientUseCasePortInput(
                "John", "Doe", "john@example.com", "A client", List.of("https://linkedin.com/in/johndoe")
        );

        // when
        var result = clientService.createClient(input);

        // then
        assertThat(result.id()).isEqualTo(GENERATED_ID);
        assertThat(result.createdAt()).isEqualTo(NOW);
        assertThat(result.updatedAt()).isEqualTo(NOW);
        assertThat(result.firstName()).isEqualTo("John");
        assertThat(result.lastName()).isEqualTo("Doe");
        assertThat(result.email()).isEqualTo("john@example.com");
        assertThat(result.description()).isEqualTo("A client");
        assertThat(result.socialLinks()).containsExactly("https://linkedin.com/in/johndoe");
    }

    @Test
    void createClient_validInput_savesClientViaPersistencePort() {
        // given
        when(uuidGeneratorPort.generate()).thenReturn(GENERATED_ID);
        when(clockPort.now()).thenReturn(NOW);

        var input = new CreateClientUseCasePortInput(
                "John", "Doe", "john@example.com", "A client", List.of()
        );

        // when
        clientService.createClient(input);

        // then
        var captor = ArgumentCaptor.forClass(Client.class);
        verify(clientPersistenceSavePort).save(captor.capture());

        var savedClient = captor.getValue();
        assertThat(savedClient.id()).isEqualTo(GENERATED_ID);
        assertThat(savedClient.email()).isEqualTo("john@example.com");
    }
}
