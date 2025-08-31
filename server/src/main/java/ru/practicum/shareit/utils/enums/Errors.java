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
    public static final String ITEM_ON_REQUEST_BY_SAME_USER = "Нельзя создавать вещь на свой же запрос";

    public static final String BOOKING_NOT_FOUND = "Бронирование не найдено";
    public static final String BOOKING_ITEM_UNAVAILABLE = "Вещь для бронирования не доступна";
    public static final String BOOKING_OWNER_NOT_BOOKER = "Хозяин вещи не может оформить бронирование";
    public static final String BOOKING_START_IS_NULL = "Дата начала бронирования не задана";
    public static final String BOOKING_END_IS_NULL = "Дата окончания бронирования не задана";
    public static final String BOOKING_START_IS_BEFORE_NOW = "Дата начала бронирования из прошлого";
    public static final String BOOKING_END_IS_BEFORE_NOW = "Дата окончания бронирования из прошлого";
    public static final String BOOKING_WRONG_RANGE = "Недопустимый диапазон дат";
    public static final String BOOKING_USER_IS_NOT_OWNER = "Пользователь не является владельцем вещи";
    public static final String BOOKING_USER_IS_NOT_OWNER_OR_BOOKER = "Пользователь не является владельцем вещи или бронирующим";
    public static final String BOOKING_STATE_NOT_DEFINED = "Параметр state не определён";

    public static final String COMMENT_NO_BOOKING = "Чтобы оставить комментарий необходимо завершённое бронирование";
    public static final String COMMENT_FROM_OWNER = "Хозяин вещи не может оставить комментарий на свою вещь";

    public static final String REQUEST_NOT_FOUND = "Запрос не найден";
    public static final String REQUEST_FORBIDDEN = "Нет доступа к данному запросу. Просмотр возможен у автора запроса и хозяина вещи";
}
