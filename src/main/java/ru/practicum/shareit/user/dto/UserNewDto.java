package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserNewDto {
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * e-mail
     */
    private String email;
}
