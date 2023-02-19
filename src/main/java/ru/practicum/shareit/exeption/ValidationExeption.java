package ru.practicum.shareit.exeption;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ValidationExeption extends RuntimeException {

    public ValidationExeption(String message) {
        super(message);
    }

    public ValidationExeption(Throwable cause) {
        super(cause);
    }


}