package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Наименование вещи
     */
    private String name;
    /**
     * Описание
     */
    private String description;
    /**
     * Флаг доступности к аренде
     */
    private Boolean available;
    /**
     * Идентификатор пользователя-владельца
     */
    private Long userId;
}
