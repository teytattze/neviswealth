package com.neviswealth.app.adapter.http.inbound.mapper;

public interface HttpDtoMapper<TDomain, THttpDto> {

    THttpDto fromDomain(TDomain domain);
}
