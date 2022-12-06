package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewLike;

import java.util.*;

@Slf4j
@Component
public class DBReviewStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    public DBReviewStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review createReview(Review review) {
        String sql = "INSERT INTO REVIEWS(ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID)" +
                "VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                review.getReviewId(),
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId()
        );
        //return review;
        return getReviewById(review.getReviewId()).get();
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE REVIEWS SET " +
                " CONTENT = ?, IS_POSITIVE = ? " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );

        return getReviewById(review.getReviewId()).get();
    }

    @Override
    public void deleteReviewById( int id) {
        String sql = "DELETE FROM REVIEWSBYLIKES WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM REVIEWS WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLike(int reviewId, int userId, boolean isLike) {
        String sql = "INSERT INTO REVIEWSBYLIKES(REVIEW_ID, IS_LIKE, USER_ID) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, reviewId, isLike, userId);
        updateUseful(reviewId, isLike, false);
    }

    @Override
    public void deleteLike(int reviewId, int userId, boolean isLike) {
        //TODO: нужно проверить, что такой like действительно есть, иначе зря изменим
        String sql = "DELETE FROM REVIEWSBYLIKES WHERE REVIEW_ID = ? AND IS_LIKE = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, reviewId, isLike, userId);
        updateUseful(reviewId, isLike, true);
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        String sql = getSqlForReviewList() + "WHERE R.ID = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, reviewId);
        List<Review> reviewsList = commonGetReviewListFromRs(rs);
        if (reviewsList.size() != 0) {
            return Optional.of(reviewsList.get(0));
        } else {
            return Optional.empty();
        }
    }
    @Override
    public List<Review> getReviewsByFilmId(Optional<Integer> filmId, int count) {
        String sql = "SELECT * FROM REVIEWS";

        if(filmId.isPresent()) {
            sql = sql + " WHERE FILM_ID = " + filmId.get();
        }
        sql = sql + " ORDER BY USEFUL DESC LIMIT " + count;
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        LinkedHashMap<Integer, Review> reviewMap = new LinkedHashMap<>();
        sql = "SELECT * FROM REVIEWSBYLIKES WHERE REVIEW_ID IN (" ;
        while(rs.next()) {
            Review currentReview = createReviewByRs(rs);
            reviewMap.put(currentReview.getReviewId(), currentReview);
            sql = sql + currentReview.getReviewId() + ",";
        }
        if(reviewMap.size() == 0) {
            return new ArrayList<>(reviewMap.values());
        }
        sql = sql.substring(0, sql.length() - 1) + ")";
        SqlRowSet rs1 = jdbcTemplate.queryForRowSet(sql);
        while (rs1.next()) {
            int reviewId = rs1.getInt("REVIEW_ID");
            if(reviewId != 0 ) {
                ReviewLike reviewLike = new ReviewLike();
                reviewLike.setUserId(rs1.getInt("USER_ID"));
                reviewLike.setLike(rs1.getBoolean("IS_LIKE"));
                reviewMap.get(reviewId).addLike(reviewLike);
            }
        }
        return new ArrayList<>(reviewMap.values());
    }

    private List<Review> commonGetReviewListFromRs(SqlRowSet rs) {
        HashMap<Integer, Review> reviewList = new HashMap<>();
        Review currentReview;
        while (rs.next()) {
            int reviewId = rs.getInt("ID");
            if (reviewList.containsKey(reviewId)) {
                currentReview = reviewList.get(reviewId);
            } else {
                currentReview = createReviewByRs(rs);
            }
            //фильм создали
            int userId = rs.getInt("RL_USER_ID");
            if (userId != 0) {
                ReviewLike reviewLike = new ReviewLike();
                reviewLike.setUserId(userId);
                reviewLike.setLike(rs.getBoolean("RL_IS_LIKE"));
                currentReview.addLike(reviewLike);
            }
            reviewList.put(reviewId, currentReview);
        }
        return new ArrayList<>(reviewList.values());
    }

    private String getSqlForReviewList() {
        return "SELECT R.*, RL.IS_LIKE RL_IS_LIKE, RL.USER_ID RL_USER_ID " +
                "FROM REVIEWS R LEFT JOIN  REVIEWSBYLIKES RL ON R.ID = RL.REVIEW_ID ";
    }

    private Review createReviewByRs(SqlRowSet rs) {
        Review review = new Review();
        review.setReviewId(rs.getInt("ID"));
        review.setContent(rs.getString("CONTENT"));
        review.setIsPositive(rs.getBoolean("IS_POSITIVE"));
        review.setUserId(rs.getInt("USER_ID"));
        review.setFilmId(rs.getInt("FILM_ID"));
        review.setUseful(rs.getInt("USEFUL"));
        return review;
    }


    private void updateUseful(int reviewId, boolean isLike, boolean isCancel) {
        int k = 1;
        if(!isCancel) {
            if (!isLike) {
                k = -1;
            }
        } else {
            if (isLike) {
                k = -1;
            }
        }

        String sql = "UPDATE REVIEWS SET USEFUL = USEFUL + ? WHERE ID = ?";
        jdbcTemplate.update(sql, k, reviewId);
    }




}

