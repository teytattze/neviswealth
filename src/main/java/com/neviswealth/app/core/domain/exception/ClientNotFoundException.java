package com.neviswealth.app.core.domain.exception;

import java.util.UUID;

public class ClientNotFoundException extends DomainException {

    public ClientNotFoundException(UUID clientId) {
        super("Client not found: " + clientId);
    }
}
