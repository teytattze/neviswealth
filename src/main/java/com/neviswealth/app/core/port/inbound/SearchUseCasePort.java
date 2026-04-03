package com.neviswealth.app.core.port.inbound;

import com.neviswealth.app.core.domain.SearchResult;

public interface SearchUseCasePort {
    SearchResult search(SearchUseCasePortInput input);
}
