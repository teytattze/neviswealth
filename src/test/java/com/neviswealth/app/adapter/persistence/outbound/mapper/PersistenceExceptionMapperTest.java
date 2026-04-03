package com.neviswealth.app.adapter.persistence.outbound.mapper;

import com.neviswealth.app.core.exception.CoreExceptionCode;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;

class PersistenceExceptionMapperTest {

    private final PersistenceExceptionMapper mapper = new PersistenceExceptionMapper();

    @Test
    void map_duplicateKeyException_returnsMongoDuplicateKey() {
        // given
        var exception = new DuplicateKeyException("Duplicate key error");

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.MONGO_DUPLICATE_KEY);
        assertThat(result.getMessage()).isEqualTo("A resource with the same unique field already exists");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_dataAccessException_returnsMongoDataAccessFailure() {
        // given
        var exception = new DataAccessResourceFailureException("Connection refused");

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.MONGO_DATA_ACCESS_FAILURE);
        assertThat(result.getMessage()).isEqualTo("Failed to access the database");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_unknownException_returnsUnknown() {
        // given
        var exception = new RuntimeException("Something unexpected");

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.UNKNOWN);
        assertThat(result.getMessage()).isEqualTo("An unexpected error occurred");
        assertThat(result.getCause()).isEqualTo(exception);
    }

    @Test
    void map_duplicateKeyException_takePrecedenceOverDataAccessException() {
        // given — DuplicateKeyException is a subclass of DataAccessException
        var exception = new DuplicateKeyException("Duplicate email");

        // when
        var result = mapper.map(exception);

        // then
        assertThat(result.getCode()).isEqualTo(CoreExceptionCode.MONGO_DUPLICATE_KEY);
    }
}
