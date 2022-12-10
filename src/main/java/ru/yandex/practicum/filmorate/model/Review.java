package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;

@Data
public class Review {
    private int reviewId;
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer filmId;
    private int useful;
    private LinkedHashSet<ReviewLike> reviewLikes = new LinkedHashSet<>();

    public void addLike(ReviewLike like) {
        reviewLikes.add(like);
    }
}
