package com.neviswealth.app.adapter.persistence.outbound.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "clients")
public class ClientModel {
    @Id
    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;

    @TextIndexed
    private final String firstName;
    @TextIndexed
    private final String lastName;
    @Indexed(unique = true)
    @TextIndexed
    private final String email;
    @TextIndexed
    private final String description;
    private final List<String> socialLinks;

    public ClientModel(UUID id, Instant createdAt, Instant updatedAt, String firstName, String lastName, String email, String description, List<String> socialLinks) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.socialLinks = socialLinks;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getSocialLinks() {
        return socialLinks;
    }
}
