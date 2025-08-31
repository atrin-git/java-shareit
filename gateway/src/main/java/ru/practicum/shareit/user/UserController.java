package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> get(@Positive @NotNull @PathVariable Long id) {
        log.info("Передан запрос на получение пользователя с id = {}", id);
        return userClient.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @NotNull @RequestBody UserRequestDto user) {
        log.info("Передан запрос на создание пользователя {}", user);
        return userClient.createUser(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> edit(@Positive @NotNull @PathVariable Long id,
                                       @Valid @NotNull @RequestBody UserUpdateRequestDto user) {
        log.info("Получен запрос на изменение пользователя {}", user);
        return userClient.editUser(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @NotNull @PathVariable Long id) {
        log.info("Получен запрос за удаление пользователя с id = {}", id);
        userClient.deleteUser(id);
    }
}
