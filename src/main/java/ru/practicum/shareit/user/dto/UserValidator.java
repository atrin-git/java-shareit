package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.ValidationException;

import static ru.practicum.shareit.utils.enums.Errors.USER_EMAIL_ERROR;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserValidator {
    public static void checkNewUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            errorEmail();
        }
    }

    public static void checkUpdateUser(User user) {
        if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail().contains("@")) {
            errorEmail();
        }
    }

    private static void errorEmail() {
        log.warn("Падение по валидации. Поле: email");
        throw new ValidationException(USER_EMAIL_ERROR);
    }
}
