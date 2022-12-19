package ru.practicum.shareit.exeption;

public class RequestException extends RuntimeException{
    public RequestException(String message) {
        super(message);
    }

}
