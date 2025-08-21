package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.utils.advices.exceptions.ValidationException;

import java.time.Instant;

import static ru.practicum.shareit.utils.enums.Errors.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingValidator {
    public static void checkBookingDates(Booking booking) {
        if (booking.getStart() == null) {
            log.warn("Падение по валидации. Поле: start is null");
            throw new ValidationException(BOOKING_START_IS_NULL);
        }
        if (booking.getEnd() == null) {
            log.warn("Падение по валидации. Поле: end is null");
            throw new ValidationException(BOOKING_END_IS_NULL);
        }
        if (booking.getStart().isBefore(Instant.now())) {
            log.warn("Падение по валидации. Поле: start < now()");
            throw new ValidationException(BOOKING_START_IS_BEFORE_NOW);
        }
        if (booking.getEnd().isBefore(Instant.now())) {
            log.warn("Падение по валидации. Поле: end < now()");
            throw new ValidationException(BOOKING_END_IS_BEFORE_NOW);
        }
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().equals(booking.getStart())) {
            log.warn("Падение по валидации. Поле: start >= end");
            throw new ValidationException(BOOKING_WRONG_RANGE);
        }
    }
}
