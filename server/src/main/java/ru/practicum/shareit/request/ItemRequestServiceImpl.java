package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestNewDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.advices.exceptions.ForbiddenException;
import ru.practicum.shareit.utils.beans.BaseService;

import java.time.Instant;
import java.util.List;

import static ru.practicum.shareit.utils.enums.Errors.REQUEST_FORBIDDEN;

@Slf4j
@Service
public class ItemRequestServiceImpl extends BaseService implements ItemRequestService {
    @Override
    public ItemRequestDto create(Long userId, ItemRequestNewDto requestNewDto) {
        final User user = getRawUser(userId);

        ItemRequest request = ItemRequest.from(requestNewDto);
        request.setRequester(user);
        request.setCreated(Instant.now());

        itemRequestRepository.save(request);

        return ItemRequestDto.from(request);
    }

    @Override
    public List<ItemRequestWithItemsDto> getAll(Long userId) {
        getRawUser(userId);

        return itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId)
                .stream()
                .map(ItemRequestWithItemsDto::from)
                .peek(request -> {
                    request.setItems(
                            itemRepository.findAllByRequestId(request.getId())
                                    .stream()
                                    .map(ItemDto::from)
                                    .toList());
                })
                .toList();
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllForUser(Long userId) {
        getRawUser(userId);

        return itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId)
                .stream()
                .map(ItemRequestWithItemsDto::from)
                .peek(request -> {
                    request.setItems(
                            itemRepository.findAllByRequestId(request.getId())
                                    .stream()
                                    .map(ItemDto::from)
                                    .toList());
                })
                .toList();
    }

    @Override
    public ItemRequestWithItemsDto getRequest(Long userId, Long requestId) {
        getRawUser(userId);

        ItemRequest request = getRawRequest(requestId);
        List<Item> items = itemRepository.findAllByRequestId(requestId);

        if (!request.getRequester().getId().equals(userId) && !items.stream().map(item -> item.getOwner().getId()).toList().contains(userId)) {
            throw new ForbiddenException(REQUEST_FORBIDDEN);
        }

        final ItemRequestWithItemsDto requestWithItems = ItemRequestWithItemsDto.from(request);
        requestWithItems.setItems(itemRepository.findAllByRequestId(requestId).stream().map(ItemDto::from).toList());
        return requestWithItems;
    }
}
