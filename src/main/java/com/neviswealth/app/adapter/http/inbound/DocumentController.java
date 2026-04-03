package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.CreateDocumentRequestBody;
import com.neviswealth.app.adapter.http.inbound.dto.DocumentResponse;
import com.neviswealth.app.adapter.http.inbound.mapper.HttpDtoMapper;
import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.port.inbound.CreateDocumentUseCasePort;
import com.neviswealth.app.core.port.inbound.CreateDocumentUseCasePortInput;
import com.neviswealth.app.core.port.inbound.GetClientByIdUseCasePort;
import com.neviswealth.app.core.port.inbound.GetClientByIdUseCasePortInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients/{clientId}/documents")
@Tag(name = "Documents", description = "Document management for clients")
public class DocumentController {

    private final CreateDocumentUseCasePort createDocumentUseCasePort;
    private final GetClientByIdUseCasePort getClientByIdUseCasePort;
    private final HttpDtoMapper<Document, DocumentResponse> documentHttpDtoMapper;

    public DocumentController(
            CreateDocumentUseCasePort createDocumentUseCasePort,
            GetClientByIdUseCasePort getClientByIdUseCasePort,
            HttpDtoMapper<Document, DocumentResponse> documentHttpDtoMapper) {
        this.createDocumentUseCasePort = createDocumentUseCasePort;
        this.getClientByIdUseCasePort = getClientByIdUseCasePort;
        this.documentHttpDtoMapper = documentHttpDtoMapper;
    }

    @PostMapping
    @Operation(
            summary = "Create a document",
            description = "Creates a new document associated with the specified client"
    )
    @ApiResponse(responseCode = "200", description = "Document created successfully")
    DocumentResponse createDocument(
            @Parameter(description = "ID of the client that owns the document")
            @PathVariable UUID clientId,
            @Valid @RequestBody CreateDocumentRequestBody body
    ) {
        var getClientByIdUseCasePortInput = new GetClientByIdUseCasePortInput(clientId);
        getClientByIdUseCasePort.getClientById(getClientByIdUseCasePortInput);

        var createDocumentUseCasePortInput = new CreateDocumentUseCasePortInput(
                body.title(),
                body.content(),
                clientId
        );
        var document = this.createDocumentUseCasePort.createDocument(createDocumentUseCasePortInput);
        return this.documentHttpDtoMapper.fromDomain(document);
    }
}
