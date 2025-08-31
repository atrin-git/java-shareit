package ru.practicum.shareit.item.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequestDto {
    @Nullable
    private String name;
    @Nullable
    private String description;
    @Nullable
    private Boolean available;
    @Nullable
    @Positive
    private Long requestId;
}
