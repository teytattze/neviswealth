package com.neviswealth.app.adapter.persistence.outbound.mapper;

import com.neviswealth.app.core.exception.CoreException;
import com.neviswealth.app.core.exception.CoreExceptionCode;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class PersistenceExceptionMapper implements ExceptionMapper {

    @Override
    public CoreException map(Exception exception) {
        if (exception instanceof DuplicateKeyException) {
            return new CoreException(
                    CoreExceptionCode.MONGO_DUPLICATE_KEY,
                    "A resource with the same unique field already exists",
                    exception
            );
        }
        if (exception instanceof DataAccessException) {
            return new CoreException(
                    CoreExceptionCode.MONGO_DATA_ACCESS_FAILURE,
                    "Failed to access the database",
                    exception
            );
        }
        return new CoreException(
                CoreExceptionCode.UNKNOWN,
                "An unexpected error occurred",
                exception
        );
    }
}
