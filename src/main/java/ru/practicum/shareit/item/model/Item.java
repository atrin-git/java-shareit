package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Item {
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
    private Boolean isAvailable;
    /**
     * Идентификатор пользователя-владельца
     */
    private Long ownerId;
}
