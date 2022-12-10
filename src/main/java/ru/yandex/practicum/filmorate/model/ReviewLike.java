package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class ReviewLike {

    private int userId;
    private boolean isLike;

}