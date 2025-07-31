package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    /**
     * Получение списка вещей пользователя
     *
     * @param userId Идентификатор пользователя
     * @return Список вещей
     */
    List<Item> getAll(long userId);

    /**
     * Получение информации о конкретной вещи
     *
     * @param itemId Идентификатор вещи
     * @return Данные о вещи
     */
    Optional<Item> get(long itemId);

    /**
     * Поиск
     *
     * @param searchString Строка для поиска
     * @return Найденные вещи
     */
    List<Item> search(String searchString);

    /**
     * Создание новой вещи
     *
     * @param item Данные о вещи
     * @return Созданная вещь
     */
    Optional<Item> create(Item item);

    /**
     * Редактирование вещи
     *
     * @param item Данные для обновления
     * @return Обновлённая вещь
     */
    Optional<Item> edit(Item item);

    /**
     * Удаление вещи
     *
     * @param itemId Идентификатор вещи
     */
    void delete(long itemId);
}
