package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :searchString, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchString, '%'))) " +
            "AND i.isAvailable = true")
    List<Item> search(String searchString);

    List<Item> findAllByRequestId(Long requestId);
//    Item findByOwnerIdAndRequestId(Long ownerId, Long requestId);
}

