package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {
    /**
     * Получение данных пользователя
     *
     * @param userId Идентификатор пользователя
     * @return Данные пользователя
     */
    Optional<User> get(long userId);

    /**
     * Получение идентификатор пользователя по e-mail
     *
     * @param email e-mail
     * @return Идентификатор пользователя
     */
    Optional<Long> getId(String email);

    /**
     * Добавление нового пользователя
     *
     * @param user Данные пользователя
     * @return Данные добавленного пользователя
     */
    Optional<User> create(User user);

    /**
     * Обновление данных пользователя
     *
     * @param user Новые данные пользователя
     * @return Обновлённые данные пользователя
     */
    Optional<User> edit(User user);

    /**
     * Удаление пользователя
     *
     * @param userId Идентификатор пользователя
     */
    void delete(long userId);
}
