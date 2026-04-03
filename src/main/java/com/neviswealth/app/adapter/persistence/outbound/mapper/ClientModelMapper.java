package com.neviswealth.app.adapter.persistence.outbound.mapper;

import com.neviswealth.app.adapter.persistence.outbound.model.ClientModel;
import com.neviswealth.app.core.domain.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientModelMapper implements ModelMapper<Client, ClientModel> {
    public Client toDomain(ClientModel model) {
        return new Client(
                model.getId(),
                model.getCreatedAt(),
                model.getUpdatedAt(),

                model.getFirstName(),
                model.getLastName(),
                model.getEmail(),
                model.getDescription(),
                model.getSocialLinks()
        );
    }

    public ClientModel fromDomain(Client domain) {
        return new ClientModel(
                domain.id(),
                domain.createdAt(),
                domain.updatedAt(),

                domain.firstName(),
                domain.lastName(),
                domain.email(),
                domain.description(),
                domain.socialLinks()
        );
    }
}
