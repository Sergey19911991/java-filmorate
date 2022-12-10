package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;


public interface ReviewStorage {

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReviewById(int id);

    void addLike(int reviewId, int userId, boolean isLike);

    Optional<Review> getReviewById(int reviewId);

    void deleteLike(int reviewId, int userId, boolean isLike);

    List<Review> getReviewsByFilmId(Optional<Integer> filmId, int count);
}
