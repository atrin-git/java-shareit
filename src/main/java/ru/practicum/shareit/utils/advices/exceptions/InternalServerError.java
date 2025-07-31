package ru.practicum.shareit.utils.advices.exceptions;

public class InternalServerError extends RuntimeException {
    public InternalServerError(String message) {
        super(message);
    }
}
