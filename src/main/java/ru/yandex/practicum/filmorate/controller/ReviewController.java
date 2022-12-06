package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final String defaultCount = "10";

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToReview(@PathVariable int id, @PathVariable int userId) {
        reviewService.addLike(id, userId, true);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeToReview(@PathVariable int id, @PathVariable int userId) {
        reviewService.addLike(id, userId, false);
    }

    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable int id) {
        reviewService.deleteReviewById(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public List<Review> getReviewList(@RequestParam(value = "filmId", required = false) Integer filmId,
                                      @RequestParam(value = "count", required = false, defaultValue = defaultCount) int count) {
        if (filmId == null) {
            return reviewService.getReviews(count);
        } else {
            return reviewService.getReviewsByFilmId(filmId, count);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDisLike(@PathVariable int id, @PathVariable int userId) {
        reviewService.deleteDisLike(id, userId);
    }

}

