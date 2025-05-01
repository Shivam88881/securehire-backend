// src/main/java/com/curtin/securehire/exception/ValidationException.java
package com.curtin.securehire.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(String message) {
        super(message);
        this.errors = null;
    }

    public List<String> getErrors() {
        return errors;
    }
}
