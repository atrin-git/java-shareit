package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserNewDto;

public interface UserService {

    /**
     * Получение данных пользователя
     *
     * @param userId Идентификатор пользователя
     * @return Данные пользователя
     */
    UserDto get(long userId);

    /**
     * Добавление нового пользователя
     *
     * @param user Данные пользователя
     * @return Данные добавленного пользователя
     */
    UserDto create(UserNewDto user);

    /**
     * Обновление данных пользователя
     *
     * @param userId Идентификатор изменяемого пользователя
     * @param user   Новые данные пользователя
     * @return Обновлённые данные пользователя
     */
    UserDto edit(long userId, UserNewDto user);

    /**
     * Удаление пользователя
     *
     * @param userId Идентификатор пользователя
     */
    void delete(long userId);
}
