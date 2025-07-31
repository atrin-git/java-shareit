package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User mapToUser(UserNewDto newUserDto) {
        return User.builder()
                .name(newUserDto.getName())
                .email(newUserDto.getEmail())
                .build();
    }

    public static User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static User mapUpdateUser(User user, User existedUser) {
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
}
