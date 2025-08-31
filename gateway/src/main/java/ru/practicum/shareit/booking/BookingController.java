package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import static ru.practicum.shareit.utils.RequestParams.X_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                         @Valid @RequestBody BookingRequestDto booking) {
        log.info("Получен запрос от пользователя {} на добавление нового бронирования для вещи {}", userId, booking);
        return bookingClient.bookItem(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> approve(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                          @Positive @NotNull @PathVariable Long bookingId,
                                          @NotNull @RequestParam("approved") Boolean isApproved) {
        log.info("Получен запрос от пользователя {} на approved = {} бронирования {}", userId, isApproved, bookingId);
        return bookingClient.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> get(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                      @Positive @NotNull @PathVariable Long bookingId) {
        log.info("Получен запрос на бронирование {} от пользователя {}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getByOwner(@Positive @NotNull @RequestHeader(X_USER_ID) Long ownerId,
                                             @NotNull @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос на получение списка бронирований c state = {}, где владелец с id = {}", state, ownerId);
        return bookingClient.getBookingByOwner(ownerId, state);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getByBooker(@Positive @NotNull @RequestHeader(X_USER_ID) Long bookerId,
                                              @NotNull @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        log.info("Получен запрос на получение списка бронирований с state = {} пользователя с id = {}", state, bookerId);
        return bookingClient.getBookingByBooker(bookerId, state);
    }
}
