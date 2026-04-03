package com.neviswealth.app.adapter.ai.outbound;

import com.neviswealth.app.adapter.ai.outbound.dto.EmbeddingRequest;
import com.neviswealth.app.adapter.ai.outbound.dto.EmbeddingResponse;
import com.neviswealth.app.adapter.ai.outbound.dto.OpenAiModel;
import com.neviswealth.app.adapter.ai.outbound.mapper.ExceptionMapper;
import com.neviswealth.app.core.port.outbound.EmbeddingGeneratorPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenAiEmbeddingAdapter implements EmbeddingGeneratorPort {

    private final RestClient openAiRestClient;
    private final OpenAiModel model;
    private final ExceptionMapper exceptionMapper;

    public OpenAiEmbeddingAdapter(
            RestClient openAiRestClient,
            @Value("${openai.embedding-model}") OpenAiModel model,
            ExceptionMapper exceptionMapper
    ) {
        this.openAiRestClient = openAiRestClient;
        this.model = model;
        this.exceptionMapper = exceptionMapper;
    }

    @Override
    public double[] generate(String text) {
        try {
            var request = new EmbeddingRequest(text, model);

            var response = openAiRestClient.post()
                    .uri("/v1/embeddings")
                    .body(request)
                    .retrieve()
                    .body(EmbeddingResponse.class);

            return response.data().getFirst().embedding().stream().mapToDouble(Double::doubleValue).toArray();
        } catch (Exception ex) {
            throw exceptionMapper.map(ex);
        }
    }
}
