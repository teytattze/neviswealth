package com.neviswealth.app.core.service;

import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePort;
import com.neviswealth.app.core.port.inbound.CreateClientUseCasePortInput;
import com.neviswealth.app.core.port.outbound.ClientPersistenceSavePort;
import com.neviswealth.app.core.port.outbound.ClockPort;
import com.neviswealth.app.core.port.outbound.UuidGeneratorPort;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements CreateClientUseCasePort {

    private final ClientPersistenceSavePort clientPersistenceSavePort;
    private final UuidGeneratorPort uuidGeneratorPort;
    private final ClockPort clockPort;

    public ClientService(
            ClientPersistenceSavePort clientPersistenceSavePort,
            UuidGeneratorPort uuidGeneratorPort,
            ClockPort clockPort
    ) {
        this.clientPersistenceSavePort = clientPersistenceSavePort;
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
}
