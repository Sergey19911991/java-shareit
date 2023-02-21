package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.RequestException;
import ru.practicum.shareit.exception.ValidationExeption;
import ru.practicum.shareit.exception.ConflictException;

@ControllerAdvice
public class ErrorHandlerTest {
    private ErrorHandler errorHandler = new ErrorHandler();


    @ExceptionHandler(value
            = {NotFoundException.class})
    public ResponseEntity<Object> handleConflict(NotFoundException e) {
        return new ResponseEntity<>(errorHandler.handleNotFoundException(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value
            = {ValidationExeption.class})
    public ResponseEntity<Object> handleValid(ValidationExeption e) {
        return new ResponseEntity<>(errorHandler.handleValidationException(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value
            = {RequestException.class})
    public ResponseEntity<Object> handleRequest(RequestException e) {
        return new ResponseEntity<>(errorHandler.handleRequestException(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value
            = {ConflictException.class})
    public ResponseEntity<Object> handleRequest(ConflictException e) {
        return new ResponseEntity<>(errorHandler.handleConflictException(e), HttpStatus.CONFLICT);
    }
}
