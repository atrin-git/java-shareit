package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

import static ru.practicum.shareit.utils.enums.RequestParams.X_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@NotNull @RequestHeader(X_USER_ID) Long userId,
                             @NotNull @RequestBody BookingNewDto booking) {
        log.info("Получен запрос на добавление нового бронирования для вещи с id = {}", booking.getItemId());
        return bookingService.create(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approve(@NotNull @RequestHeader(X_USER_ID) Long userId,
                              @NotNull @PathVariable Long bookingId,
                              @NotNull @RequestParam("approved") Boolean isApproved) {
        log.info("Получен запрос на подтверждение или отклонение бронирования с id = {}", bookingId);
        return bookingService.approve(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto get(@NotNull @RequestHeader(X_USER_ID) Long userId,
                          @NotNull @PathVariable Long bookingId) {
        log.info("Получен запрос на бронирование с id = {} от пользователя с id = {}", bookingId, userId);
        return bookingService.get(userId, bookingId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getByOwner(@NotNull @RequestHeader(X_USER_ID) Long ownerId,
                                       @NotNull @RequestParam(name = "state", defaultValue = "ALL") State state) {
        log.info("Получен запрос на получение списка бронирований, где владелец с id = {}", ownerId);
        return bookingService.getByOwner(ownerId, state);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getByBooker(@NotNull @RequestHeader(X_USER_ID) Long bookerId,
                                        @NotNull @RequestParam(name = "state", defaultValue = "ALL") State state) {
        log.info("Получен запрос на получение списка бронирований пользователя с id = {}", bookerId);
        return bookingService.getByBooker(bookerId, state);
    }
}
