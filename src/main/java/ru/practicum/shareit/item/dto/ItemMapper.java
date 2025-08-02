package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .userId(item.getOwnerId())
                .build();
    }

    public static Item mapToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .isAvailable(itemDto.getAvailable())
                .ownerId(itemDto.getUserId())
                .build();
    }

    public static Item mapToItem(ItemNewDto itemNewDto) {
        return Item.builder()
                .name(itemNewDto.getName())
                .description(itemNewDto.getDescription())
                .isAvailable(itemNewDto.getAvailable())
                .build();
    }

    public static Item mapUpdateItem(Item item, Item exsitedItem) {
        return Item.builder()
                .id(exsitedItem.getId())
                .name(
                        (item.getName() != null && !item.getName().isEmpty())
                                ? item.getName()
                                : exsitedItem.getName()
                )
                .description(
                        (item.getDescription() != null && !item.getDescription().isEmpty())
                                ? item.getDescription()
                                : exsitedItem.getDescription()
                )
                .isAvailable(item.getIsAvailable() != null ? item.getIsAvailable() : exsitedItem.getIsAvailable())
                .ownerId(exsitedItem.getOwnerId())
                .build();
    }
}
