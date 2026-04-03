package com.neviswealth.app.core.domain.exception;

import java.util.UUID;

public class DocumentNotFoundException extends DomainException {

    public DocumentNotFoundException(UUID documentId) {
        super("Document not found: " + documentId);
    }
}
