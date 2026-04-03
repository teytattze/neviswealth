package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.ClientResponse;
import com.neviswealth.app.adapter.http.inbound.mapper.HttpDtoMapper;
import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateClientUseCasePort createClientUseCase;

    @MockitoBean
    private HttpDtoMapper<Client, ClientResponse> clientHttpDtoMapper;

    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final Instant NOW = Instant.parse("2026-01-15T10:30:00Z");

    @Test
    void createClient_validRequest_returns200WithClientResponse() throws Exception {
        // given
        var client = Client.create(CLIENT_ID, NOW, "John", "Doe", "john@example.com", "A client", List.of());
        var response = new ClientResponse(CLIENT_ID, NOW, NOW, "John", "Doe", "john@example.com", "A client", List.of());

        when(createClientUseCase.createClient(any())).thenReturn(client);
        when(clientHttpDtoMapper.fromDomain(client)).thenReturn(response);

        // when / then
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "email": "john@example.com",
                                    "description": "A client",
                                    "socialLinks": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CLIENT_ID.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createClient_missingFirstName_returns422() throws Exception {
        // given
        var requestBody = """
                {
                    "firstName": "",
                    "lastName": "Doe",
                    "email": "john@example.com"
                }
                """;

        // when / then
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createClient_invalidEmail_returns422() throws Exception {
        // given
        var requestBody = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "not-an-email"
                }
                """;

        // when / then
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity());
    }
}
