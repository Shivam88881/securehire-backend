// src/main/java/com/curtin/securehire/exception/NotFoundException.java
package com.curtin.securehire.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
