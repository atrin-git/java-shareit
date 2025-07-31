package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ItemNewDto {
    /**
     * Название вещи
     */
    private String name;
    /**
     * Описание вещи
     */
    private String description;
    /**
     * Флаг доступности вещи к аренде
     */
    private Boolean available;
}
