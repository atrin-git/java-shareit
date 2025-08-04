package ru.practicum.shareit.utils.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.utils.advices.exceptions.DuplicateException;
import ru.practicum.shareit.utils.advices.exceptions.ForbiddenException;
import ru.practicum.shareit.utils.advices.exceptions.NotFoundException;
import ru.practicum.shareit.utils.advices.exceptions.ValidationException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorTemplate getValidationException(final ValidationException e) {
        return new ErrorTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorTemplate getNotFoundException(final NotFoundException e) {
        return new ErrorTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorTemplate getValidationException(final DuplicateException e) {
        return new ErrorTemplate(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorTemplate getForbiddenException(final ForbiddenException e) {
        return new ErrorTemplate(e.getMessage());
    }
}
