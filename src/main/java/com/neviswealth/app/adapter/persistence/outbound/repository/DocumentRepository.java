package com.neviswealth.app.adapter.persistence.outbound.repository;

import com.neviswealth.app.adapter.persistence.outbound.model.DocumentModel;
import org.springframework.data.domain.Score;
import org.springframework.data.domain.SearchResults;
import org.springframework.data.domain.Vector;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.VectorSearch;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentModel, UUID> {

    @VectorSearch(indexName = "document_embedding_index", numCandidates = "200", limit = "10")
    SearchResults<DocumentModel> searchByEmbeddingNear(Vector vector, Score score);

}
