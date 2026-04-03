package com.neviswealth.app.core.domain.exception;

public class DuplicateClientEmailException extends DomainException {

    public DuplicateClientEmailException(String email) {
        super("A client with email '" + email + "' already exists");
    }
}
