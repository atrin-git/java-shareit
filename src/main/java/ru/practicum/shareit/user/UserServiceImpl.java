package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserNewDto;
import ru.practicum.shareit.user.dto.UserValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.DuplicateException;
import ru.practicum.shareit.utils.advices.exceptions.NotFoundException;

import static ru.practicum.shareit.utils.enums.Errors.USER_DUPLICATE_EMAIL;
import static ru.practicum.shareit.utils.enums.Errors.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto get(long userId) {
        return userRepository.get(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id = {} не найден", userId);
                    return new NotFoundException(USER_NOT_FOUND);
                });
    }

    @Override
    public UserDto create(UserNewDto newUser) {
        final User user = UserMapper.mapToUser(newUser);
        UserValidator.checkNewUser(user);

        if (userRepository.getId(user.getEmail()).isPresent()) {
            log.warn("Пользователь с email = {} уже был создан ранее", newUser.getEmail());
            throw new DuplicateException(USER_DUPLICATE_EMAIL);
        }

        return userRepository.create(user)
                .map(UserMapper::mapToUserDto)
                .orElseThrow();
    }

    @Override
    public UserDto edit(long id, UserNewDto updateUser) {
        User user = UserMapper.mapToUser(updateUser);
        user.setId(id);

        UserValidator.checkUpdateUser(user);
        final User existedUser = UserMapper.mapToUser(get(id));

        userRepository.getId(user.getEmail())
                .ifPresent(existedId -> {
                    if (!existedId.equals(id)) {
                        log.error("Нарушена уникальность email = {}, у пользователя с id = {} такой же", updateUser.getEmail(), existedId);
                        throw new DuplicateException(USER_DUPLICATE_EMAIL);
                    }
                });

        user = UserMapper.mapUpdateUser(user, existedUser);

        return userRepository.edit(user)
                .map(UserMapper::mapToUserDto)
                .orElseThrow();
    }

    @Override
    public void delete(long userId) {
        get(userId);

        userRepository.delete(userId);
    }
}
