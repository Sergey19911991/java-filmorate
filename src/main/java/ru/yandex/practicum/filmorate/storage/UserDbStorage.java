package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;


@Primary
@Component
public class UserDbStorage implements UserStorage {

    private UserDao userDao;

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(UserDao userDao, JdbcTemplate jdbcTemplate) {
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        return userDao.creatUserId(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public Collection<User> findAll() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userDao.findUserById(id);
    }

    public User putFriends(int id, int friendId) {
        return userDao.putFriends(id, friendId);
    }

    public Collection<User> getFriends(int id) {
        return userDao.getFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return userDao.getCommonFriends(id, otherId);
    }

    public int deletFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDao.deleteFriend(id, friendId);
    }

    @Override
    public void deleteUserById(int userId) {
        deleteEventsUser(userId);
        deleteAllUsersReviewLikes(userId);
        deleteAllUsersReviews(userId);
        deleteAllFriendsFromUser(userId);
        deleteOnlyUser(userId);
    }

    public List<Integer> getUserFavoriteFilmsIds(int id) {
        String sqlQuery = "SELECT FILMS_ID FROM FILMS_LIKES WHERE USERS_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToInteger, id);
    }

    private Integer mapRowToInteger(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("FILMS_ID");
    }

    private void deleteOnlyUser(int userId) {
        String sql = "DELETE FROM USERS WHERE USERS_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    private void deleteAllFriendsFromUser(int userId) {
        String sql = "DELETE FROM USERS_FRIENDS WHERE USERS_ID = ? OR USERS_FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, userId);
    }

    private void deleteAllUsersReviewLikes(int userId) {
        String sql = "DELETE FROM REVIEWSBYlIKES WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    private void deleteAllUsersReviews(int userId) {
        String sql = "DELETE FROM REVIEWS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    private void deleteEventsUser(int userId){
        String sql = "DELETE FROM EVENTS WHERE USERS_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

}
