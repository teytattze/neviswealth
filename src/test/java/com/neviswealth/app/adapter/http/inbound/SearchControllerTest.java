package com.neviswealth.app.adapter.http.inbound;

import com.neviswealth.app.adapter.http.inbound.dto.SearchResponse;
import com.neviswealth.app.adapter.http.inbound.mapper.HttpDtoMapper;
import com.neviswealth.app.core.domain.SearchResult;
import com.neviswealth.app.core.port.inbound.SearchUseCasePort;
import com.neviswealth.app.core.port.inbound.SearchUseCasePortInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchUseCasePort searchUseCase;

    @MockitoBean
    private HttpDtoMapper<SearchResult, SearchResponse> searchResultMapper;

    @Test
    void search_validQuery_returns200WithSearchResponse() throws Exception {
        // given
        var searchResult = new SearchResult(List.of(), List.of());
        var searchResponse = new SearchResponse(List.of(), List.of());

        when(searchUseCase.search(any())).thenReturn(searchResult);
        when(searchResultMapper.fromDomain(searchResult)).thenReturn(searchResponse);

        // when / then
        mockMvc.perform(get("/search").param("query", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients").isArray())
                .andExpect(jsonPath("$.documents").isArray());
    }

    @Test
    void search_withIncludeSummaryTrue_passesIncludeSummaryToUseCase() throws Exception {
        // given
        var searchResult = new SearchResult(List.of(), List.of());
        var searchResponse = new SearchResponse(List.of(), List.of());

        when(searchUseCase.search(any())).thenReturn(searchResult);
        when(searchResultMapper.fromDomain(searchResult)).thenReturn(searchResponse);

        // when
        mockMvc.perform(get("/search")
                        .param("query", "test")
                        .param("includeSummary", "true"))
                .andExpect(status().isOk());

        // then
        verify(searchUseCase).search(new SearchUseCasePortInput("test", true));
    }
}
