package com.neviswealth.app.adapter.persistence.outbound;

import com.neviswealth.app.adapter.persistence.outbound.mapper.ExceptionMapper;
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
    private final ExceptionMapper exceptionMapper;

    public ClientPersistenceAdapter(
            ClientRepository clientRepository,
            ModelMapper<Client, ClientModel> clientModelMapper,
            ExceptionMapper exceptionMapper
    ) {
        this.clientRepository = clientRepository;
        this.clientModelMapper = clientModelMapper;
        this.exceptionMapper = exceptionMapper;
    }

    @Override
    public void save(Client domain) {
        try {
            var model = clientModelMapper.fromDomain(domain);
            this.clientRepository.save(model);
        } catch (Exception ex) {
            throw exceptionMapper.map(ex);
        }
    }

    @Override
    public List<Client> searchByText(String text) {
        try {
            var criteria = TextCriteria.forDefaultLanguage().matchingAny(text);
            var result = this.clientRepository.findAllBy(criteria);
            return result.stream().map(this.clientModelMapper::toDomain).toList();
        } catch (Exception ex) {
            throw exceptionMapper.map(ex);
        }
    }
}
