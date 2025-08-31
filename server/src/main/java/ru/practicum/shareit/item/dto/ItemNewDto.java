package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemNewDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
