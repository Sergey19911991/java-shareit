package ru.practicum.shareit.exeption;

public class ValidationExeption extends RuntimeException {

    public ValidationExeption(String message) {
        super(message);
    }

    public ValidationExeption(Throwable cause) {
        super(cause);
    }


}