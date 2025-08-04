package ru.practicum.shareit.utils.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestParams {
    public static final String X_USER_ID = "X-Sharer-User-Id";
}
