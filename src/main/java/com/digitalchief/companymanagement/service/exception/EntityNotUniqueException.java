package com.digitalchief.companymanagement.service.exception;

public class EntityNotUniqueException extends RuntimeException {
    public EntityNotUniqueException(String message) {
        super(message);
    }
}
