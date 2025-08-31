package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDto get(Long userId, Long bookingId);

    BookingDto create(Long userId, BookingNewDto booking);

    BookingDto approve(Long userId, Long bookingId, Boolean isApproved);

    List<BookingDto> getByOwner(Long userId, State state);

    List<BookingDto> getByBooker(Long userId, State state);
}
