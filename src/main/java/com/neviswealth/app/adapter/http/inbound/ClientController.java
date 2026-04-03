package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.ClientResponse;
import com.neviswealth.app.adapter.http.inbound.dto.CreateClientRequestBody;
import com.neviswealth.app.adapter.http.inbound.mapper.HttpDtoMapper;
import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePort;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePortInput;
import com.neviswealth.app.adapter.http.inbound.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "409", description = "Client with this email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Downstream service error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
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
