package ru.practicum.shareit.booking;

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
public class BookingServerController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(X_USER_ID) Long userId,
                             @RequestBody BookingNewDto booking) {
        log.info("Получен запрос на добавление нового бронирования для вещи с id = {}", booking.getItemId());
        return bookingService.create(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approve(@RequestHeader(X_USER_ID) Long userId,
                              @PathVariable Long bookingId,
                              @RequestParam("approved") Boolean isApproved) {
        log.info("Получен запрос на подтверждение или отклонение бронирования с id = {}", bookingId);
        return bookingService.approve(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto get(@RequestHeader(X_USER_ID) Long userId,
                          @PathVariable Long bookingId) {
        log.info("Получен запрос на бронирование с id = {} от пользователя с id = {}", bookingId, userId);
        return bookingService.get(userId, bookingId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getByOwner(@RequestHeader(X_USER_ID) Long ownerId,
                                       @RequestParam(name = "state", defaultValue = "ALL") State state) {
        log.info("Получен запрос на получение списка бронирований, где владелец с id = {}", ownerId);
        return bookingService.getByOwner(ownerId, state);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getByBooker(@RequestHeader(X_USER_ID) Long bookerId,
                                        @RequestParam(name = "state", defaultValue = "ALL") State state) {
        log.info("Получен запрос на получение списка бронирований пользователя с id = {}", bookerId);
        return bookingService.getByBooker(bookerId, state);
    }
}
