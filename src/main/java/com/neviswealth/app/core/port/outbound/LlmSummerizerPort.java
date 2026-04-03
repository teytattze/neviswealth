package com.neviswealth.app.core.port.outbound;

public interface LlmSummerizerPort {
    String summarize(String content, String prompt);
}
