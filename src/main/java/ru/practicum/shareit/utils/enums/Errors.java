package ru.practicum.shareit.utils.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Errors {
    public static final String USER_NOT_FOUND = "Пользователь не найден";
    public static final String USER_DUPLICATE_EMAIL = "Пользователь с этим email уже существует";
    public static final String USER_EMAIL_ERROR = "Поле email заполнено некорректно";

    public static final String ITEM_NOT_FOUND = "Вещь не найдена";
    public static final String ITEM_NAME_ERROR = "Поле name заполнено некорректно";
    public static final String ITEM_DUPLICATE_NAME = "Вещь с таким названием уже была добавлена ранее";
    public static final String ITEM_DESCRIPTION_ERROR = "Поле description заполнено некорректно";
    public static final String ITEM_AVAILABLE_ERROR = "Поле available заполнено некорректно";
    public static final String ITEM_NO_USER = "Поле userId заполнено некорректно";
    public static final String ITEM_OF_ANOTHER_USER = "Вещь принадлежит другому пользователю. Передан некорректный идентификатор пользователя";

}
