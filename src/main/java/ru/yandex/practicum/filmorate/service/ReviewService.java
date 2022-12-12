package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.numerators.ReviewNumerator;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserDbService userService;
    private final FilmDbService filmService;
    private final EventService eventService;

    public ReviewService(ReviewStorage reviewStorage, UserDbService userService,
                         FilmDbService filmService, EventService eventService) {
        this.reviewStorage = reviewStorage;
        this.userService = userService;
        this.filmService = filmService;
        this.eventService = eventService;
    }

    public Review createReview(Review review) {
        checkFilmAndUser(review.getFilmId(), review.getUserId());
        review.setReviewId(ReviewNumerator.geReviewId());
        review = reviewStorage.createReview(review);
        log.info("Создан новый отзыв: " + review.toString());
        Event newEvent = eventService.addEvent(new Event(review.getUserId(), EventType.REVIEW.toString(),
                EventOperation.ADD.toString(), review.getReviewId()));
        log.info("В ленте новое событие - {}", newEvent);
        return getReviewById(review.getReviewId());
    }

    public Review updateReview(Review review) {
        checkFilmAndUser(review.getFilmId(), review.getUserId());
        review = reviewStorage.updateReview(review);
        log.info("Изменил отзыв: " + review.toString());
        Event newEvent = eventService.addEvent(new Event(review.getUserId(), EventType.REVIEW.toString(),
                EventOperation.UPDATE.toString(), review.getReviewId()));
        log.info("В ленте новое событие - {}", newEvent);
        return getReviewById(review.getReviewId());
    }

    public void deleteReviewById(int id) {
        Review review = getReviewById(id);
        reviewStorage.deleteReviewById(id);
        Event newEvent = eventService.addEvent(new Event(review.getUserId(), EventType.REVIEW.toString(),
                EventOperation.REMOVE.toString(), review.getReviewId()));
        log.info("В ленте новое событие - {}", newEvent);
    }

    public void addLike(int reviewId, int userId, boolean isLike) {
        //проверим, есть ли такой юзер
        User user = userService.getUser(userId);
        //TODO: проверить, есть ли отзыв....
        reviewStorage.addLike(reviewId, userId, isLike);
    }

    public Review getReviewById(int reviewId) {
        return reviewStorage.getReviewById(reviewId)
                .orElseThrow(() -> {
                    String msg = "Не могу найти отзы с Id =" + reviewId;
                    log.warn(msg);
                    throw new NotFoundException(msg);
                });
    }

    public List<Review> getReviewsByFilmId(int filmId, int count) {
        return reviewStorage.getReviewsByFilmId(Optional.of(filmId), count);
    }

    public List<Review> getReviews(int count) {
        return reviewStorage.getReviewsByFilmId(Optional.empty(), count);
    }


    public void deleteLike(int reviewId, int userId) {
        reviewStorage.deleteLike(reviewId, userId, true);
    }

    public void deleteDisLike(int reviewId, int userId) {
        reviewStorage.deleteLike(reviewId, userId, true);
    }

    private void checkFilmAndUser(int filmId, int userId) {
        User user = userService.getUser(userId);
        Film film = filmService.getFilm(filmId);
    }

}

