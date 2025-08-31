package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestReqDto;

import static ru.practicum.shareit.utils.RequestParams.X_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                                @Valid @NotNull @RequestBody ItemRequestReqDto requestDto) {
        log.info("Получен запрос от пользователя {} на создание заявки {}", userId, requestDto);
        return requestClient.createRequest(userId, requestDto);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllRequests(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId) {
        log.info("Получен запрос от пользователя {} на список всех заявок от всех пользователей", userId);
        return requestClient.getAllRequests(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllForUser(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId) {
        log.info("Получен запрос от пользователя {} на список всех его заявок", userId);
        return requestClient.getAllForUser(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequest(@Positive @NotNull @RequestHeader(X_USER_ID) Long userId,
                                             @Positive @NotNull @PathVariable Long requestId) {
        log.info("Получен запрос от пользователя {} на его заявку {}", userId, requestId);
        return requestClient.getRequest(userId, requestId);
    }
}
