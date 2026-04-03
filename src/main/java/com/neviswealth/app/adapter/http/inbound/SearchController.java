package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.SearchResponse;
import com.neviswealth.app.adapter.http.inbound.mapper.HttpDtoMapper;
import com.neviswealth.app.core.domain.SearchResult;
import com.neviswealth.app.core.port.inbound.SearchUseCasePort;
import com.neviswealth.app.core.port.inbound.SearchUseCasePortInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Tag(name = "Search", description = "Unified search across clients and documents")
public class SearchController {

    private final SearchUseCasePort searchUseCase;
    private final HttpDtoMapper<SearchResult, SearchResponse> searchResultMapper;

    public SearchController(
            SearchUseCasePort searchUseCase,
            HttpDtoMapper<SearchResult, SearchResponse> searchResultMapper
    ) {
        this.searchUseCase = searchUseCase;
        this.searchResultMapper = searchResultMapper;
    }

    @GetMapping
    @Operation(
            summary = "Search clients and documents",
            description = "Searches clients by name/email/description (text match) and documents by semantic similarity (vector search). Optionally includes AI-generated summaries of matched documents."
    )
    @ApiResponse(responseCode = "200", description = "Search results returned successfully")
    SearchResponse search(
            @Parameter(description = "Search query text") @RequestParam String query,
            @Parameter(description = "Include AI-generated document summaries") @RequestParam(required = false, defaultValue = "false") boolean includeSummary
    ) {
        var input = new SearchUseCasePortInput(query, includeSummary);
        var result = searchUseCase.search(input);
        return searchResultMapper.fromDomain(result);
    }
}
