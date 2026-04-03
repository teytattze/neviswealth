package com.neviswealth.app.core.port.inbound;

import com.neviswealth.app.core.domain.Client;

public interface CreateClientUseCasePort {
    Client createClient(CreateClientUseCasePortInput input);
}
