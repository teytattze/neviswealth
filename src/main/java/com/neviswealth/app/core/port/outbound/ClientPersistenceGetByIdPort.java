package com.neviswealth.app.core.port.outbound;

import com.neviswealth.app.core.domain.Client;

import java.util.UUID;

public interface ClientPersistenceGetByIdPort {
    Client getById(UUID id);
}
