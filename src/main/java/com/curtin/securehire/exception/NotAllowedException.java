// src/main/java/com/curtin/securehire/exception/NotAllowedException.java
package com.curtin.securehire.exception;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String message) {
        super(message);
    }

    public NotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
