package com.neviswealth.app.adapter.persistence.outbound;

import com.neviswealth.app.adapter.persistence.outbound.mapper.DocumentModelMapper;
import com.neviswealth.app.adapter.persistence.outbound.repository.DocumentRepository;
import com.neviswealth.app.core.domain.Document;
import com.neviswealth.app.core.port.outbound.DocumentPersistenceQueryPort;
import com.neviswealth.app.core.port.outbound.DocumentPersistenceSavePort;
import org.springframework.data.domain.Score;
import org.springframework.data.domain.Vector;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentPersistenceAdapter implements DocumentPersistenceSavePort, DocumentPersistenceQueryPort {

    private final DocumentRepository documentRepository;
    private final DocumentModelMapper documentModelMapper;

    public DocumentPersistenceAdapter(DocumentRepository documentRepository, DocumentModelMapper documentModelMapper) {
        this.documentRepository = documentRepository;
        this.documentModelMapper = documentModelMapper;
    }

    @Override
    public void save(Document domain) {
        var model = documentModelMapper.fromDomain(domain);
        this.documentRepository.save(model);
    }

    @Override
    public List<Document> searchByEmbedding(double[] embedding) {
        Score score = Score.of(0.5);
        Vector vector = Vector.of(embedding);
        var searchResults = this.documentRepository.searchByEmbeddingNear(vector, score);
        return searchResults.map(this.documentModelMapper::toDomain).contentStream().toList();
    }
}
