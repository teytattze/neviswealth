package com.neviswealth.app.adapter.persistence.outbound.repository;

import com.neviswealth.app.adapter.persistence.outbound.model.ClientModel;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends MongoRepository<ClientModel, UUID> {
    List<ClientModel> findAllBy(TextCriteria criteria);
}
