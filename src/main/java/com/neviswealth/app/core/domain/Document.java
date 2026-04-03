package com.neviswealth.app.core.domain;

import java.time.Instant;
import java.util.UUID;

public record Document(
        UUID id,
        Instant createdAt,
        Instant updatedAt,

        String title,
        String content,
        double[] embedding,
        String summary,

        UUID clientId
) {
    public static Document create(
            UUID id,
            Instant now,
            String title,
            String content,
            double[] embedding,
            UUID clientId
    ) {
        return new Document(id, now, now, title, content, embedding, null, clientId);
    }

    public Document withSummary(String summary) {
        return new Document(
                this.id,
                this.createdAt,
                this.updatedAt,

                this.title,
                this.content,
                this.embedding,
                summary,

                this.clientId
        );
    }
}
