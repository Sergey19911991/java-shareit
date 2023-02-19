package ru.practicum.shareit.exception;

public class RequestException extends RuntimeException {
    public RequestException(String message) {
        super(message);
    }
}
