package ru.practicum.shareit.user;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserNewDto;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto get(@NotNull @PathVariable long id) {
        log.info("Передан запрос на получение пользователя с id = {}", id);
        return userService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@NotNull @RequestBody UserNewDto user) {
        log.info("Передан запрос на создание пользователя c email = {}", user.getEmail());
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto edit(@NotNull @PathVariable long id,
                        @NotNull @RequestBody UserNewDto user) {
        log.info("Получен запрос на изменение пользователя с id = {}", id);
        return userService.edit(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NotNull @PathVariable long id) {
        log.info("Получен запрос за удаление пользователя с id = {}", id);
        userService.delete(id);
    }
}
