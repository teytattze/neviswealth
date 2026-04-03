package com.neviswealth.app.core.port.outbound;

import com.neviswealth.app.core.domain.Document;

import java.util.List;

public interface DocumentPersistenceQueryPort {
    List<Document> searchByEmbedding(double[] embedding);
}
