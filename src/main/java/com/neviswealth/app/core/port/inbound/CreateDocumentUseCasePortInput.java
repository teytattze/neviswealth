package com.neviswealth.app.core.port.inbound;

import java.util.UUID;

public record CreateDocumentUseCasePortInput(
        String title,
        String content,

        UUID clientId
) {
}
