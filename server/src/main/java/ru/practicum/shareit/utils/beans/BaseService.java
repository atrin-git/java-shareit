package ru.practicum.shareit.utils.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.NotFoundException;

import static ru.practicum.shareit.utils.enums.Errors.*;

@Slf4j
@Service
public class BaseService {
    @Autowired
    protected ItemRepository itemRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected BookingRepository bookingRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected ItemRequestRepository itemRequestRepository;

    protected User getRawUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id = {} не найден", userId);
                    return new NotFoundException(USER_NOT_FOUND);
                });
    }

    protected Item getRawItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.error("Вещь с id = {} не найдена", itemId);
                    return new NotFoundException(ITEM_NOT_FOUND);
                });
    }

    protected Booking getRawBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.error("Бронирование с id = {} не найдена", bookingId);
                    return new NotFoundException(BOOKING_NOT_FOUND);
                });
    }

    protected ItemRequest getRawRequest(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Запрос с id = {} не найден", requestId);
                    return new NotFoundException(REQUEST_NOT_FOUND);
                });
    }
}
