package com.neviswealth.app.adapter.http.inbound.mapper;

import com.neviswealth.app.adapter.http.inbound.dto.ClientResponse;
import com.neviswealth.app.core.domain.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientHttpDtoMapper implements HttpDtoMapper<Client, ClientResponse> {

    @Override
    public ClientResponse fromDomain(Client domain) {
        return new ClientResponse(
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
