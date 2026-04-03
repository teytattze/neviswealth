package com.neviswealth.app.core.port.outbound;

import com.neviswealth.app.core.domain.Client;

import java.util.List;

public interface ClientPersistenceQueryPort {
    List<Client> searchByText(String text);
}
