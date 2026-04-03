package com.neviswealth.app.adapter.persistence.outbound.config;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


@Component
public class MongoIndexInitializer {

    private static final Logger log = LoggerFactory.getLogger(MongoIndexInitializer.class);

    private final MongoTemplate mongoTemplate;

    public MongoIndexInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createVectorSearchIndex() {
        try {
            var collection = mongoTemplate.getCollection("documents");

            var existingIndexes = collection.listSearchIndexes();
            for (var index : existingIndexes) {
                if ("document_embedding_index".equals(index.getString("name"))) {
                    log.info("Vector search index 'document_embedding_index' already exists");
                    return;
                }
            }

            var indexDefinition = new Document()
                    .append("mappings", new Document()
                            .append("dynamic", false)
                            .append("fields", new Document()
                                    .append("embedding", new Document()
                                            .append("type", "knnVector")
                                            .append("dimensions", 1536)
                                            .append("similarity", "cosine"))));

            collection.createSearchIndex("document_embedding_index", indexDefinition);
            log.info("Created vector search index 'document_embedding_index'");
        } catch (Exception e) {
            log.warn("Failed to create vector search index: {}. Vector search may not be available.", e.getMessage());
        }
    }
}
