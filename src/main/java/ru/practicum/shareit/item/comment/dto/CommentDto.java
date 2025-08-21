package ru.practicum.shareit.item.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.comment.model.Comment;

import java.time.Instant;

@Getter
@Setter
@Builder(toBuilder = true)
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "UTC")
    private Instant created;

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
