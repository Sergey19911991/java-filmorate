package ru.yandex.practicum.filmorate.numerators;

public class ReviewNumerator {
    private static int reviewId;

    public static int geReviewId() {
        return ++reviewId;
    }
}