package org.example.exceptions;

public class ArrayIsFullException extends RuntimeException {
    public ArrayIsFullException(String message) {
        super(message);
    }
}
