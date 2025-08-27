package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentNewDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemNewDto;

import java.util.List;

import static ru.practicum.shareit.utils.enums.RequestParams.X_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemFullDto> getAll(@NotNull @RequestHeader(X_USER_ID) long userId) {
        log.info("Получение списка вещей пользователя с id = {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemFullDto get(@RequestHeader(X_USER_ID) long userId,
                           @NotNull @PathVariable long itemId) {
        log.info("Получение данных о вещи с id = {}", itemId);
        return itemService.get(userId, itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(X_USER_ID) long userId,
                          @NotNull @RequestBody ItemNewDto item) {
        log.info("Получение запрос на создание новой вещи \"{}\"", item.getName());
        return itemService.create(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto edit(@NotNull @RequestHeader(X_USER_ID) long userId,
                        @NotNull @PathVariable long itemId,
                        @NotNull @RequestBody ItemNewDto item) {
        log.info("Получение запрос на редактирование вещи c name = {}", itemId);
        return itemService.edit(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NotNull @RequestHeader(X_USER_ID) long userId,
                       @NotNull @PathVariable long itemId) {
        log.info("Получение запрос на удаление вещи c id = {}", itemId);
        itemService.delete(userId, itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@NotNull @RequestHeader(X_USER_ID) long userId,
                                @RequestParam("text") String searchString) {
        log.info("Получен запрос от пользователя {} на поиск вещи: {}", userId, searchString);
        return itemService.search(userId, searchString);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@NotNull @RequestHeader(X_USER_ID) long userId,
                                 @NotNull @PathVariable long itemId,
                                 @NotNull @RequestBody CommentNewDto comment) {
        log.info("Получен запрос от пользователя {} на добавление отзыва к вещи: {}", userId, itemId);
        return itemService.addComment(userId, itemId, comment);
    }

    @GetMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemFullDto getComments(@NotNull @RequestHeader(X_USER_ID) long userId,
                                   @NotNull @PathVariable long itemId) {
        log.info("Получен запрос от пользователя {} на список отзывов к вещи: {}", userId, itemId);
        return itemService.getComments(userId, itemId);
    }
}
