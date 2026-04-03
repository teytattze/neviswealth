package com.neviswealth.app.adapter.ai.outbound.mapper;

import com.neviswealth.app.core.exception.CoreException;

public interface ExceptionMapper {
    CoreException map(Exception exception);
}
