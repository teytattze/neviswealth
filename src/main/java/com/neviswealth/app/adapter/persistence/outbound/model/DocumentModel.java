package com.neviswealth.app.adapter.persistence.outbound.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.domain.Vector;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "documents")
public class DocumentModel {
    @Id
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;

    private final String title;
    private final String content;
    private final Vector embedding;

    private final UUID clientId;


    public DocumentModel(UUID id, Instant createdAt, Instant updatedAt, String title, String content, Vector embedding, UUID clientId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.title = title;
        this.content = content;
        this.embedding = embedding;
        this.clientId = clientId;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Vector getEmbedding() {
        return embedding;
    }

    public UUID getClientId() {
        return clientId;
    }
}
