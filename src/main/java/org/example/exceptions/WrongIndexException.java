package org.example.exceptions;

public class WrongIndexException extends RuntimeException {
    public WrongIndexException(String message) {
        super(message);
    }
}
