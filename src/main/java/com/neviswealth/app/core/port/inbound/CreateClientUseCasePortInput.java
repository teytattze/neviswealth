package com.neviswealth.app.core.port.inbound;

import java.util.List;

public record CreateClientUseCasePortInput(
        String firstName,
        String lastName,
        String email,
        String description,
        List<String> socialLinks
) {
}
