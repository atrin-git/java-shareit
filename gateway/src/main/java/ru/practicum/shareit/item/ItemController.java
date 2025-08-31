package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemUpdateRequestDto;

import static ru.practicum.shareit.utils.RequestParams.X_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAll(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId) {
        log.info("Получение списка вещей пользователя {}", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                          @Positive @NotNull @PathVariable Long itemId) {
        log.info("Получение данных о вещи {} для пользователя {}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                         @Valid @NotNull @RequestBody ItemRequestDto item) {
        log.info("Получение запрос от пользователя {} на создание вещи {}", userId, item);
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> edit(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                       @Positive @NotNull @PathVariable Long itemId,
                                       @Valid @NotNull @RequestBody ItemUpdateRequestDto item) {
        log.info("Получение запрос на редактирование вещи {}", item);
        return itemClient.editItem(userId, itemId, item);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                       @Positive @NotNull @PathVariable Long itemId) {
        log.info("Получение запрос на удаление вещи {}", itemId);
        itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> search(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                         @NotNull @RequestParam("text") String searchString) {
        log.info("Получен запрос от пользователя {} на поиск вещи: {}", userId, searchString);
        return itemClient.searchItem(userId, searchString);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                             @Positive @NotNull @PathVariable Long itemId,
                                             @Valid @NotNull @RequestBody CommentRequestDto comment) {
        log.info("Получен запрос от пользователя {} на добавление отзыва к вещи: {}", userId, itemId);
        return itemClient.addComment(userId, itemId, comment);
    }

    @GetMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getComments(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                              @Positive @NotNull @PathVariable Long itemId) {
        log.info("Получен запрос от пользователя {} на список отзывов к вещи: {}", userId, itemId);
        return itemClient.getComments(userId, itemId);
    }
}
