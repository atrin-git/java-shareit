package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemNewDto;

import java.util.List;

public interface ItemService {
    /**
     * Получение списка вещей пользователя
     *
     * @param userId Идентификатор пользователя
     * @return Список вещей
     */
    List<ItemDto> getAll(long userId);

    /**
     * Получение информации о конкретной вещи
     *
     * @param itemId Идентификатор вещи
     * @return Данные о вещи
     */
    ItemDto get(long itemId);

    /**
     * Поиск вещи
     *
     * @param searchString Строка поиска
     * @return Найденные соответствия
     */
    List<ItemDto> search(String searchString);

    /**
     * Создание новой вещи
     *
     * @param ownerId Идентификатор пользователя-владельца
     * @param item    Данные о вещи
     * @return Созданная вещь
     */
    ItemDto create(long ownerId, ItemNewDto item);

    /**
     * Редактирование вещи
     *
     * @param ownerId Идентификатор пользователя-владельца
     * @param itemId  Идентификатор редактируемой вещи
     * @param item    Данные для обновления
     * @return Обновлённая вещь
     */
    ItemDto edit(long ownerId, long itemId, ItemNewDto item);

    /**
     * Удаление вещи
     *
     * @param ownerId Идентификатор пользователя-владельца
     * @param itemId  Идентификатор вещи
     */
    void delete(long ownerId, long itemId);
}
