package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserNewDto;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    public static User from(UserNewDto newUserDto) {
        return User.builder()
                .name(newUserDto.getName())
                .email(newUserDto.getEmail())
                .build();
    }

    public static User from(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static User from(User user, User existedUser) {
        return User.builder()
                .id(existedUser.getId())
                .email(
                        (user.getEmail() != null && !user.getEmail().isEmpty())
                                ? user.getEmail()
                                : existedUser.getEmail()
                )
                .name(
                        (user.getName() != null && !user.getName().isEmpty())
                                ? user.getName()
                                : existedUser.getName())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
