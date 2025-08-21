package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserNewDto;
import ru.practicum.shareit.user.dto.UserValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.DuplicateException;
import ru.practicum.shareit.utils.beans.BaseService;

import static ru.practicum.shareit.utils.enums.Errors.USER_DUPLICATE_EMAIL;

@Slf4j
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Override
    public UserDto get(Long userId) {
        final User user = getRawUser(userId);
        return UserDto.from(user);
    }

    @Override
    public UserDto create(UserNewDto newUser) {
        final User user = User.from(newUser);
        UserValidator.checkNewUser(user);

        if (userRepository.findByEmail(user.getEmail()) != null) {
            log.warn("Пользователь с email = {} уже был создан ранее", newUser.getEmail());
            throw new DuplicateException(USER_DUPLICATE_EMAIL);
        }

        userRepository.save(user);

        return UserDto.from(user);
    }

    @Override
    public UserDto edit(Long id, UserNewDto updateUser) {
        User user = User.from(updateUser);
        user.setId(id);

        UserValidator.checkUpdateUser(user);
        final User existedUser = getRawUser(id);

        final User sameEmailUser = userRepository.findByEmail(user.getEmail());
        if (sameEmailUser != null && !sameEmailUser.getId().equals(id)) {
            log.error("Нарушена уникальность email = {}, у пользователя с id = {} такой же", updateUser.getEmail(), sameEmailUser.getId());
            throw new DuplicateException(USER_DUPLICATE_EMAIL);
        }

        user = User.from(user, existedUser);
        userRepository.save(user);

        return UserDto.from(user);
    }

    @Override
    public void delete(Long userId) {
        getRawUser(userId);
        userRepository.deleteById(userId);
    }
}
