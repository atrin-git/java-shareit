package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utils.advices.exceptions.ValidationException;

import static ru.practicum.shareit.utils.enums.Errors.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemValidator {
    public static void checkNewItem(Item newItem) {
        if (newItem.getName() == null || newItem.getName().isEmpty()) {
            log.warn("Падение по валидации. Поле: name");
            throw new ValidationException(ITEM_NAME_ERROR);
        }
        if (newItem.getDescription() == null || newItem.getDescription().isEmpty()) {
            log.warn("Падение по валидации. Поле: description");
            throw new ValidationException(ITEM_DESCRIPTION_ERROR);
        }
        if (newItem.getIsAvailable() == null) {
            log.warn("Падение по валидации. Поле: available");
            throw new ValidationException(ITEM_AVAILABLE_ERROR);
        }
        if (newItem.getOwner().getId() == null) {
            log.warn("Падение по валидации. Поле: userId");
            throw new ValidationException(ITEM_NO_USER);
        }
    }
}
