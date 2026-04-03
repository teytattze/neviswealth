package com.neviswealth.app.core.port.inbound;

import com.neviswealth.app.core.domain.Document;

public interface CreateDocumentUseCasePort {
    Document createDocument(CreateDocumentUseCasePortInput input);
}
