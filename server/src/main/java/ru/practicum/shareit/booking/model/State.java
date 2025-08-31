package ru.practicum.shareit.booking.model;

import lombok.Getter;

@Getter
public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;
}
