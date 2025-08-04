package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.utils.advices.exceptions.DuplicateException;
import ru.practicum.shareit.utils.advices.exceptions.ForbiddenException;
import ru.practicum.shareit.utils.advices.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.practicum.shareit.utils.enums.Errors.*;

@Slf4j
@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 0L;

    @Override
    public List<Item> getAll(long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .toList();
    }

    @Override
    public Optional<Item> get(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Optional<Item> create(Item item) {
        if (items.values().stream().anyMatch(i -> i.getName().equals(item.getName()))) {
            log.error("Вещь с названием {} уже была добавлена ранее", item.getName());
            throw new DuplicateException(ITEM_DUPLICATE_NAME);
        }

        item.setId(++nextId);
        items.put(item.getId(), item);
        return get(item.getId());
    }

    @Override
    public Optional<Item> edit(Item item) {
        Item existedItem = items.get(item.getId());

        if (existedItem == null) {
            log.error("Вещь с id = {} не найдена", item.getId());
            throw new NotFoundException(ITEM_NOT_FOUND);
        }

        if (!existedItem.getOwnerId().equals(item.getOwnerId())) {
            log.error("Ранее созданная вещь в БД принадлежит пользователю с id = {}, а передан id = {}", existedItem.getOwnerId(), existedItem.getId());
            throw new ForbiddenException(ITEM_OF_ANOTHER_USER);
        }

        items.put(item.getId(), item);
        return get(item.getId());
    }

    @Override
    public void delete(long itemId) {
        items.remove(itemId);
    }

    @Override
    public List<Item> search(String searchString) {
        final String text = searchString.toLowerCase();
        return items.values()
                .stream()
                .filter(item -> (item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                        && item.getIsAvailable().equals(true))
                .toList();
    }
}
