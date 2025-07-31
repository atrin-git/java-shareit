package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        item.setId(++nextId);
        items.putIfAbsent(item.getId(), item);
        return get(item.getId());
    }

    @Override
    public Optional<Item> edit(Item item) {
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
