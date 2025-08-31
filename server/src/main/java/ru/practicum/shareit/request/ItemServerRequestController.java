package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

import static ru.practicum.shareit.utils.enums.RequestParams.X_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/requests")
public class ItemServerRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createRequest(@RequestHeader(X_USER_ID) Long userId,
                                        @RequestBody ItemRequestNewDto requestNewDto) {
        log.info("Получен запрос на создание заявки от пользователя с id = {}", userId);
        return itemRequestService.create(userId, requestNewDto);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getAllRequests(@RequestHeader(X_USER_ID) Long userId) {
        log.info("Получен запрос от пользователя с id = {} на список всех заявок от всех пользователей", userId);
        return itemRequestService.getAll(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestWithItemsDto> getAllForUser(@RequestHeader(X_USER_ID) Long userId) {
        log.info("Получен запрос от пользователя с id = {} на список всех его заявок", userId);
        return itemRequestService.getAllForUser(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestWithItemsDto getRequest(@RequestHeader(X_USER_ID) Long userId,
                                              @PathVariable Long requestId) {
        log.info("Получен запрос от пользователя с id = {} на его заявку с id = {}", userId, requestId);
        return itemRequestService.getRequest(userId, requestId);
    }
}
