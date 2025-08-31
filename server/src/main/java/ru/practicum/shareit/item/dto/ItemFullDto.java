package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class ItemFullDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Instant lastBooking;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Instant nextBooking;
    private List<CommentDto> comments;

    public static ItemFullDto from(Item item) {
        return ItemFullDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .userId(item.getOwner().getId())
                .build();
    }
}
