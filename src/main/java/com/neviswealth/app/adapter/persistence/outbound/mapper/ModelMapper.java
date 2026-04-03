package com.neviswealth.app.adapter.persistence.outbound.mapper;

public interface ModelMapper<TDomain, TModel> {
    TDomain toDomain(TModel model);

    TModel fromDomain(TDomain domain);
}
