package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentNewDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemNewDto;

import java.util.List;

public interface ItemService {

    List<ItemFullDto> getAll(Long userId);

    ItemFullDto get(Long userId, Long itemId);

    List<ItemDto> search(Long userId, String searchString);

    ItemDto create(Long ownerId, ItemNewDto item);

    ItemDto edit(Long ownerId, Long itemId, ItemNewDto item);

    void delete(Long ownerId, Long itemId);

    CommentDto addComment(Long userId, Long itemId, CommentNewDto comment);

    ItemFullDto getComments(Long userId, Long itemId);
}
