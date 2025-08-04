package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class User {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * e-mail
     */
    private String email;
}
