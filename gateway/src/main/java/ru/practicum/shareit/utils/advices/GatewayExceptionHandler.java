package ru.practicum.shareit.utils.advices;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.utils.advices.dto.Error;
import ru.practicum.shareit.utils.advices.dto.ErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<Error> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new Error(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ErrorResponse("Валидация не пройдена", errors),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            ConstraintViolationException ex) {

        List<Error> errors = ex.getConstraintViolations()
                .stream()
                .map(error -> new Error(error.getPropertyPath().toString(), error.getMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new ErrorResponse("Валидация не пройдена", errors),
                HttpStatus.BAD_REQUEST
        );
    }
}