package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.ClientResponse;
import com.neviswealth.app.adapter.http.inbound.dto.CreateClientRequestBody;
import com.neviswealth.app.adapter.http.inbound.mapper.HttpDtoMapper;
import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePort;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePortInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Client management")
public class ClientController {

    private final CreateClientUseCasePort createClientUseCasePort;
    private final HttpDtoMapper<Client, ClientResponse> clientHttpDtoMapper;

    public ClientController(
            CreateClientUseCasePort createClientUseCasePort,
            HttpDtoMapper<Client, ClientResponse> clientHttpDtoMapper
    ) {
        this.createClientUseCasePort = createClientUseCasePort;
        this.clientHttpDtoMapper = clientHttpDtoMapper;
    }

    @PostMapping
    @Operation(
            summary = "Create a client",
            description = "Creates a new client with the provided details and social links"
    )
    @ApiResponse(responseCode = "200", description = "Client created successfully")
    ClientResponse createClient(@Valid @RequestBody CreateClientRequestBody body) {
        var createClientUseCasePortInput = new CreateClientUseCasePortInput(
                body.firstName(),
                body.lastName(),
                body.email(),
                body.description(),
                body.socialLinks()
        );
        var client = this.createClientUseCasePort.createClient(createClientUseCasePortInput);
        return this.clientHttpDtoMapper.fromDomain(client);
    }
}
