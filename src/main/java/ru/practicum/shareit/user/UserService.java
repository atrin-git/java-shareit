package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserNewDto;

public interface UserService {
    UserDto get(Long userId);

    UserDto create(UserNewDto user);

    UserDto edit(Long userId, UserNewDto user);

    void delete(Long userId);
}
