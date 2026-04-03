package com.neviswealth.app.core.service;

import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.exception.CoreException;
import com.neviswealth.app.core.exception.CoreExceptionCode;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePort;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePortInput;
import com.neviswealth.app.core.port.inbound.GetClientByIdUseCasePort;
import com.neviswealth.app.core.port.inbound.GetClientByIdUseCasePortInput;
import com.neviswealth.app.core.port.outbound.ClientPersistenceGetByIdPort;
import com.neviswealth.app.core.port.outbound.ClientPersistenceSavePort;
import com.neviswealth.app.core.port.outbound.ClockPort;
import com.neviswealth.app.core.port.outbound.UuidGeneratorPort;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements CreateClientUseCasePort, GetClientByIdUseCasePort {

    private final ClientPersistenceSavePort clientPersistenceSavePort;
    private final ClientPersistenceGetByIdPort clientPersistenceGetByIdPort;
    private final UuidGeneratorPort uuidGeneratorPort;
    private final ClockPort clockPort;

    public ClientService(
            ClientPersistenceSavePort clientPersistenceSavePort,
            ClientPersistenceGetByIdPort clientPersistenceGetByIdPort,
            UuidGeneratorPort uuidGeneratorPort,
            ClockPort clockPort
    ) {
        this.clientPersistenceSavePort = clientPersistenceSavePort;
        this.clientPersistenceGetByIdPort = clientPersistenceGetByIdPort;
        this.uuidGeneratorPort = uuidGeneratorPort;
        this.clockPort = clockPort;
    }

    @Override
    public Client createClient(CreateClientUseCasePortInput input) {
        var client = Client.create(
                uuidGeneratorPort.generate(),
                clockPort.now(),
                input.firstName(),
                input.lastName(),
                input.email(),
                input.description(),
                input.socialLinks()
        );
        this.clientPersistenceSavePort.save(client);
        return client;
    }

    @Override
    public Client getClientById(GetClientByIdUseCasePortInput input) {
        var client = this.clientPersistenceGetByIdPort.getById(input.clientId());
        if (client == null) {
            throw new CoreException(CoreExceptionCode.CLIENT_NOT_FOUND, "Client not found", null);
        }
        return client;
    }
}
