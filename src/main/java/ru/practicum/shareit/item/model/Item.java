package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemNewDto;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@ToString
@Entity
@Table(name = "items", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public static Item from(ItemNewDto itemNewDto) {
        return Item.builder()
                .name(itemNewDto.getName())
                .description(itemNewDto.getDescription())
                .isAvailable(itemNewDto.getAvailable())
                .build();
    }

    public static Item from(Item item, Item exsitedItem) {
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
                .owner(exsitedItem.getOwner())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        return id != null && id.equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
