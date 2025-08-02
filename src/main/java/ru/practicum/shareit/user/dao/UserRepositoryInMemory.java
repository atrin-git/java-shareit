package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.DuplicateException;
import ru.practicum.shareit.utils.advices.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.practicum.shareit.utils.enums.Errors.USER_DUPLICATE_EMAIL;
import static ru.practicum.shareit.utils.enums.Errors.USER_NOT_FOUND;

@Slf4j
@Repository
public class UserRepositoryInMemory implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> usersByEmails = new HashMap<>();
    private long nextId = 0L;

    @Override
    public Optional<User> get(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<Long> getId(String email) {
        return Optional.ofNullable(usersByEmails.get(email));
    }

    @Override
    public Optional<User> create(User user) {
        if (usersByEmails.containsKey(user.getEmail())) {
            log.error("Пользователь с email = {} уже существует", user.getEmail());
            throw new DuplicateException(USER_DUPLICATE_EMAIL);
        }

        user.setId(++nextId);
        users.put(user.getId(), user);
        usersByEmails.put(user.getEmail(), user.getId());
        return get(user.getId());
    }

    @Override
    public Optional<User> edit(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователя с id = {} не существует", user.getId());
            throw new NotFoundException(USER_NOT_FOUND);
        }

        Long existedUserId = usersByEmails.get(user.getEmail());
        if (existedUserId != null && !existedUserId.equals(user.getId())) {
            log.error("У пользователя с id = {} такой же email = {}", existedUserId, user.getEmail());
            throw new DuplicateException(USER_DUPLICATE_EMAIL);
        }

        usersByEmails.remove(users.get(user.getId()).getEmail());
        users.put(user.getId(), user);
        usersByEmails.put(user.getEmail(), user.getId());

        return get(user.getId());
    }

    @Override
    public void delete(long userId) {
        usersByEmails.remove(users.get(userId).getEmail());
        users.remove(userId);
    }
}

