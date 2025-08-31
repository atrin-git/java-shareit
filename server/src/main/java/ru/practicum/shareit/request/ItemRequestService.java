package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestNewDto requestNewDto);

    List<ItemRequestWithItemsDto> getAll(Long userId);

    List<ItemRequestWithItemsDto> getAllForUser(Long userId);

    ItemRequestWithItemsDto getRequest(Long userId, Long requestId);
}
