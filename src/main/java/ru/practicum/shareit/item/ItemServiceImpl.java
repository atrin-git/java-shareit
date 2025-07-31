package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemNewDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.utils.advices.exceptions.InternalServerError;
import ru.practicum.shareit.utils.advices.exceptions.NotFoundException;
import ru.practicum.shareit.utils.advices.exceptions.ValidationException;

import java.util.List;

import static ru.practicum.shareit.item.dto.ItemValidator.checkNewItem;
import static ru.practicum.shareit.utils.enums.Errors.*;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemRepository.getAll(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto get(long itemId) {
        return itemRepository.get(itemId)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND));
    }

    @Override
    public ItemDto create(long userId, ItemNewDto newItem) {
        userService.get(userId);

        final Item item = ItemMapper.mapToItem(newItem);
        item.setOwnerId(userId);
        checkNewItem(item);

        return itemRepository.create(item)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> {
                    log.error("Не удалось создать новую вещь");
                    return new InternalServerError(ITEM_CREATION_ERROR);
                });
    }

    @Override
    public ItemDto edit(long userId, long itemId, ItemNewDto updateItem) {
        userService.get(userId);

        Item item = ItemMapper.mapToItem(updateItem);
        final Item itemExists = ItemMapper.mapToItem(get(itemId));

        if (!itemExists.getOwnerId().equals(userId)) {
            log.error("Ранее созданная вещь в БД принадлежит пользователю с id = {}, а передан id = {}", itemExists.getOwnerId(), userId);
            throw new ValidationException(ITEM_OF_ANOTHER_USER);
        }

        item = ItemMapper.mapUpdateItem(item, itemExists);

        return itemRepository.edit(item)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> new InternalServerError(ITEM_UPDATE_ERROR));
    }

    @Override
    public void delete(long userId, long itemId) {
        userService.get(userId);
        get(itemId);

        itemRepository.delete(itemId);
    }

    @Override
    public List<ItemDto> search(String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(searchString)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
