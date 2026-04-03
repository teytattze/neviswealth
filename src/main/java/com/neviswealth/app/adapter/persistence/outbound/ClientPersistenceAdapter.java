package com.neviswealth.app.adapter.persistence.outbound;

import com.neviswealth.app.adapter.persistence.outbound.mapper.ModelMapper;
import com.neviswealth.app.adapter.persistence.outbound.model.ClientModel;
import com.neviswealth.app.adapter.persistence.outbound.repository.ClientRepository;
import com.neviswealth.app.core.domain.Client;
import com.neviswealth.app.core.port.outbound.ClientPersistenceQueryPort;
import com.neviswealth.app.core.port.outbound.ClientPersistenceSavePort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientPersistenceAdapter implements ClientPersistenceSavePort, ClientPersistenceQueryPort {

    private final ClientRepository clientRepository;
    private final ModelMapper<Client, ClientModel> clientModelMapper;

    public ClientPersistenceAdapter(ClientRepository clientRepository, ModelMapper<Client, ClientModel> clientModelMapper) {
        this.clientRepository = clientRepository;
        this.clientModelMapper = clientModelMapper;
    }

    @Override
    public void save(Client domain) {
        var model = clientModelMapper.fromDomain(domain);
        this.clientRepository.save(model);
    }

    @Override
    public List<Client> searchByText(String text) {
        var criteria = TextCriteria.forDefaultLanguage().matchingAny(text);
        var result = this.clientRepository.findAllBy(criteria);
        return result.stream().map(this.clientModelMapper::toDomain).toList();
    }
}
