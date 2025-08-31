package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingNewDto;
import ru.practicum.shareit.booking.dto.BookingValidator;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.ForbiddenException;
import ru.practicum.shareit.utils.advices.exceptions.ValidationException;
import ru.practicum.shareit.utils.beans.BaseService;

import java.util.List;

import static ru.practicum.shareit.utils.enums.Errors.*;

@Slf4j
@Service
public class BookingServiceImpl extends BaseService implements BookingService {

    @Override
    public BookingDto get(Long userId, Long bookingId) {
        final Booking booking = getRawBooking(bookingId);

        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            log.error("Пользователь с id = {} не является бронирующим или владельцем для вещи с id = {}",
                    userId, booking.getItem().getId());
            throw new ForbiddenException(BOOKING_USER_IS_NOT_OWNER_OR_BOOKER);
        }

        return BookingDto.from(booking);
    }

    @Override
    public BookingDto create(Long userId, BookingNewDto bookingNew) {
        final Booking booking = Booking.from(bookingNew);
        BookingValidator.checkBookingDates(booking);

        final User user = getRawUser(userId);
        final Item item = getRawItem(bookingNew.getItemId());

        if (!item.getIsAvailable()) {
            log.error("Вещь с id = {} не доступна для бронирования", userId);
            throw new ValidationException(BOOKING_ITEM_UNAVAILABLE);
        }

        if (userId.equals(item.getOwner().getId())) {
            log.error("Пользователь с id = {} не является бронирующим вещь", userId);
            throw new ValidationException(BOOKING_OWNER_NOT_BOOKER);
        }

        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.WAITING);

        bookingRepository.save(booking);

        return BookingDto.from(booking);
    }

    @Override
    public BookingDto approve(Long userId, Long bookingId, Boolean isApproved) {
        final Booking booking = getRawBooking(bookingId);

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            log.error("Пользователь с id = {} не является владельцем вещи", userId);
            throw new ForbiddenException(BOOKING_USER_IS_NOT_OWNER);
        }

        booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);

        bookingRepository.save(booking);

        return BookingDto.from(booking);
    }

    @Override
    public List<BookingDto> getByOwner(Long ownerId, State state) {
        getRawUser(ownerId);
        final List<Booking> bookingList;

        switch (state) {
            case ALL -> {
                bookingList = bookingRepository.findByItemOwnerId(ownerId);
            }
            case CURRENT -> {
                bookingList = bookingRepository.findByOwnerCurrentBookings(ownerId);
            }
            case PAST -> {
                bookingList = bookingRepository.findByOwnerPastBookings(ownerId);
            }
            case FUTURE -> {
                bookingList = bookingRepository.findByOwnerFutureBookings(ownerId);
            }
            case WAITING -> {
                bookingList = bookingRepository.findByItemOwnerIdAndStatus(ownerId, Status.WAITING);
            }
            case REJECTED -> {
                bookingList = bookingRepository.findByItemOwnerIdAndStatus(ownerId, Status.REJECTED);
            }
            default -> {
                log.error("Передано несуществующее значение для state = {}", state);
                throw new ValidationException(BOOKING_STATE_NOT_DEFINED);
            }
        }

        return bookingList.stream().map(BookingDto::from).toList();
    }


    @Override
    public List<BookingDto> getByBooker(Long bookerId, State state) {
        getRawUser(bookerId);
        final List<Booking> bookingList;

        switch (state) {
            case ALL -> {
                bookingList = bookingRepository.findByBookerId(bookerId);
            }
            case CURRENT -> {
                bookingList = bookingRepository.findByBookerCurrentBookings(bookerId);
            }
            case PAST -> {
                bookingList = bookingRepository.findByBookerPastBookings(bookerId);
            }
            case FUTURE -> {
                bookingList = bookingRepository.findByBookerFutureBookings(bookerId);
            }
            case WAITING -> {
                bookingList = bookingRepository.findByBookerIdAndStatus(bookerId, Status.WAITING);
            }
            case REJECTED -> {
                bookingList = bookingRepository.findByBookerIdAndStatus(bookerId, Status.REJECTED);
            }
            default -> {
                log.error("Передано несуществующее значение для state = {}", state);
                throw new ValidationException(BOOKING_STATE_NOT_DEFINED);
            }
        }

        return bookingList.stream().map(BookingDto::from).toList();
    }
}
