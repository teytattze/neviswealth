package com.neviswealth.app.core.port.outbound;

import com.neviswealth.app.core.domain.Client;

public interface ClientPersistenceSavePort {
    void save(Client data);
}
