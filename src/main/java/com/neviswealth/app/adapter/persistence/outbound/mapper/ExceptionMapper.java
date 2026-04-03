package com.neviswealth.app.adapter.persistence.outbound.mapper;

import com.neviswealth.app.core.exception.CoreException;

public interface ExceptionMapper {
    CoreException map(Exception exception);
}
