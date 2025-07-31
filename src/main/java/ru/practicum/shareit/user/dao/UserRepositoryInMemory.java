package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        user.setId(++nextId);
        users.putIfAbsent(user.getId(), user);
        usersByEmails.putIfAbsent(user.getEmail(), user.getId());
        return get(user.getId());
    }

    @Override
    public Optional<User> edit(User user) {
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

