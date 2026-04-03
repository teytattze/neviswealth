package com.neviswealth.app.core.port.outbound;

import com.neviswealth.app.core.domain.Document;

public interface DocumentPersistenceSavePort {
    void save(Document data);
}
