package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.DocumentResponse;
import com.neviswealth.app.adapter.http.inbound.mapper.HttpDtoMapper;
import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.port.inbound.CreateDocumentUseCasePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateDocumentUseCasePort createDocumentUseCase;

    @MockitoBean
    private HttpDtoMapper<Document, DocumentResponse> documentHttpDtoMapper;

    private static final UUID CLIENT_ID = UUID.randomUUID();
    private static final UUID DOCUMENT_ID = UUID.randomUUID();
    private static final Instant NOW = Instant.parse("2026-01-15T10:30:00Z");

    @Test
    void createDocument_validRequest_returns200WithDocumentResponse() throws Exception {
        // given
        var document = Document.create(DOCUMENT_ID, NOW, "Title", "Content", new double[]{0.1}, CLIENT_ID);
        var response = new DocumentResponse(DOCUMENT_ID, NOW, NOW, "Title", "Content", null, CLIENT_ID);

        when(createDocumentUseCase.createDocument(any())).thenReturn(document);
        when(documentHttpDtoMapper.fromDomain(document)).thenReturn(response);

        // when / then
        mockMvc.perform(post("/clients/{clientId}/documents", CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Title",
                                    "content": "Content"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DOCUMENT_ID.toString()))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.content").value("Content"))
                .andExpect(jsonPath("$.clientId").value(CLIENT_ID.toString()));
    }

    @Test
    void createDocument_missingTitle_returns422() throws Exception {
        // given
        var requestBody = """
                {
                    "title": "",
                    "content": "Content"
                }
                """;

        // when / then
        mockMvc.perform(post("/clients/{clientId}/documents", CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDocument_missingContent_returns422() throws Exception {
        // given
        var requestBody = """
                {
                    "title": "Title",
                    "content": ""
                }
                """;

        // when / then
        mockMvc.perform(post("/clients/{clientId}/documents", CLIENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity());
    }
}
