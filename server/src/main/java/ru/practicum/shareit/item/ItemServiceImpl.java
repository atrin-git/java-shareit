package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentNewDto;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemNewDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.ForbiddenException;
import ru.practicum.shareit.utils.advices.exceptions.ValidationException;
import ru.practicum.shareit.utils.beans.BaseService;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.item.dto.ItemValidator.checkNewItem;
import static ru.practicum.shareit.utils.enums.Errors.*;

@Slf4j
@Service
public class ItemServiceImpl extends BaseService implements ItemService {
    @Override
    public List<ItemFullDto> getAll(Long userId) {
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemFullDto::from)
                .peek(item -> {
                    setBookingDates(item, userId);
                    setComments(item);
                })
                .toList();
    }

    @Override
    public ItemFullDto get(Long userId, Long itemId) {
        getRawUser(userId);

        ItemFullDto item = ItemFullDto.from(getRawItem(itemId));
        setBookingDates(item, userId);
        setComments(item);

        return item;
    }

    @Override
    public ItemDto create(Long userId, ItemNewDto newItem) {
        final User user = getRawUser(userId);
        final Item item = Item.from(newItem);

        item.setOwner(user);
        checkNewItem(item);

        if (newItem.getRequestId() != null) {
            final ItemRequest request = getRawRequest(newItem.getRequestId());
            if (!Objects.equals(request.getRequester().getId(), userId)) {
                item.setRequest(request);
            } else {
                throw new ValidationException(ITEM_ON_REQUEST_BY_SAME_USER);
            }
        }

        itemRepository.save(item);

        return ItemDto.from(getRawItem(item.getId()));
    }

    @Override
    public ItemDto edit(Long userId, Long itemId, ItemNewDto updateItem) {
        getRawUser(userId);

        Item item = Item.from(updateItem);
        final Item itemExists = getRawItem(itemId);

        if (!itemExists.getOwner().getId().equals(userId)) {
            log.error("Ранее созданная вещь в БД принадлежит пользователю с id = {}, а передан id = {}", itemExists.getOwner().getId(), userId);
            throw new ForbiddenException(ITEM_OF_ANOTHER_USER);
        }

        item = Item.from(item, itemExists);

        itemRepository.save(item);

        return ItemDto.from(getRawItem(itemId));
    }

    @Override
    public void delete(Long userId, Long itemId) {
        getRawUser(userId);
        getRawItem(itemId);

        itemRepository.deleteById(itemId);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentNewDto commentNew) {
        final User author = getRawUser(userId);
        final Item item = getRawItem(itemId);

        final List<Booking> bookings = bookingRepository.findLastBookingsFotItem(userId, itemId);

        if (bookings == null || bookings.isEmpty()) {
            log.error("Бронирование для вещи id = {} у пользователя с id = {} не найдено", itemId, userId);
            throw new ValidationException(COMMENT_NO_BOOKING);
        }

        if (author.getId().equals(item.getOwner().getId())) {
            log.error("Пользователь с id = {} не может оставить отзыв на свою вещь с id = {}", userId, itemId);
            throw new ValidationException(COMMENT_FROM_OWNER);
        }

        final Comment comment = Comment.from(commentNew);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(Instant.now());

        commentRepository.save(comment);

        return CommentDto.from(comment);
    }

    @Override
    public ItemFullDto getComments(Long userId, Long itemId) {
        getRawUser(userId);

        final ItemFullDto item = ItemFullDto.from(getRawItem(itemId));
        setComments(item);

        return item;
    }

    @Override
    public List<ItemDto> search(Long userId, String searchString) {
        getRawUser(userId);

        if (searchString == null || searchString.isEmpty()) {
            log.warn("Передана пустая строка поиска: \"{}\"", searchString);
            return List.of();
        }
        return itemRepository.search(searchString)
                .stream()
                .map(ItemDto::from)
                .toList();
    }

    private void setBookingDates(ItemFullDto item, Long userId) {
        List<Booking> lastBookings = bookingRepository.findLastBookings(item.getId(), userId);
        if (lastBookings.stream().findFirst().isPresent()) {
            item.setLastBooking(lastBookings.stream().findFirst().get().getStart());
        }

        List<Booking> nextBookings = bookingRepository.findNextBookings(item.getId(), userId);
        if (nextBookings.stream().findFirst().isPresent()) {
            item.setNextBooking(nextBookings.stream().findFirst().get().getStart());
        }
    }

    private void setComments(ItemFullDto item) {
        item.setComments(commentRepository.findByItemId(item.getId())
                .stream()
                .map(CommentDto::from)
                .toList());
    }

}
