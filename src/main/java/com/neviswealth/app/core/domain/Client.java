package com.neviswealth.app.core.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Client(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String firstName,
        String lastName,
        String email,
        String description,
        List<String> socialLinks
) {
    public static Client create(
            UUID id,
            Instant now,
            String firstName,
            String lastName,
            String email,
            String description,
            List<String> socialLinks
    ) {
        return new Client(id, now, now, firstName, lastName, email, description, socialLinks);
    }
}
