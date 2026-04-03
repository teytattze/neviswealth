package com.neviswealth.app.core.port.inbound;

public record SearchUseCasePortInput(
        String query,
        boolean includeSummary
) {
}
