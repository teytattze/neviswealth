package com.neviswealth.app.adapter.ai.outbound;

import com.neviswealth.app.adapter.ai.outbound.dto.ChatMessageRequest;
import com.neviswealth.app.adapter.ai.outbound.dto.ChatRequest;
import com.neviswealth.app.adapter.ai.outbound.dto.ChatResponse;
import com.neviswealth.app.adapter.ai.outbound.dto.ChatRole;
import com.neviswealth.app.adapter.ai.outbound.dto.OpenAiModel;
import com.neviswealth.app.adapter.ai.outbound.mapper.ExceptionMapper;
import com.neviswealth.app.core.port.outbound.LlmSummerizerPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OpenAiLlmAdapter implements LlmSummerizerPort {

    private final RestClient openAiRestClient;
    private final OpenAiModel model;
    private final ExceptionMapper exceptionMapper;

    public OpenAiLlmAdapter(
            RestClient openAiRestClient,
            @Value("${openai.chat-model}") OpenAiModel model,
            ExceptionMapper exceptionMapper
    ) {
        this.openAiRestClient = openAiRestClient;
        this.model = model;
        this.exceptionMapper = exceptionMapper;
    }

    @Override
    public String summarize(String content, String query) {
        try {
            var messages = List.of(
                    new ChatMessageRequest(ChatRole.SYSTEM, "Summarize the following document content in 2-3 concise sentences, focusing on information relevant to the user's query."),
                    new ChatMessageRequest(ChatRole.USER, "Query: " + query + "\n\nDocument content:\n" + content)
            );
            var request = new ChatRequest(model, messages);

            var response = openAiRestClient.post()
                    .uri("/v1/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(ChatResponse.class);

            return response.choices().getFirst().message().content();
        } catch (Exception ex) {
            throw exceptionMapper.map(ex);
        }
    }
}
