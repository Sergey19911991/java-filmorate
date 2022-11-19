package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Component
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("USERS_ID"))
                .name(resultSet.getString("USERS_NAME"))
                .login(resultSet.getString("USERS_LOGIN"))
                .email(resultSet.getString("USERS_EMAIL"))
                .birthday(resultSet.getDate("USERS_BIRTHDAY").toLocalDate())
                .build();
    }

    public User creatUserId(@Valid User user) {
        try {
            validationUser(user);
            String sqlQuery = "insert into USERS(USERS_NAME,USERS_LOGIN,USERS_EMAIL,USERS_BIRTHDAY) " +
                    "values (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USERS_ID"});
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getEmail());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                return stmt;
            }, keyHolder);
            log.info("Добавлен пользователь с id = {}", keyHolder.getKey().intValue());
            user.setId(keyHolder.getKey().intValue());
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return user;
    }

    public User updateUser(@Valid User user) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from USERS where USERS_ID = ?", user.getId());
        if (filmRows.next()) {
            validationUser(user);
            String sqlQuery = "update USERS set " +
                    "USERS_NAME = ?, USERS_LOGIN = ?, USERS_EMAIL = ?, USERS_BIRTHDAY = ? " +
                    "where USERS_ID = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getName()
                    , user.getLogin()
                    , user.getEmail()
                    , user.getBirthday()
                    , user.getId());
        } else {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return user;
    }

    public User findUserById(Integer id) {
        String sqlQuery = "select * from USERS where USERS_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            log.info("Найден пользователь id =  {}", id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } else {
            throw new NotFoundException("Такого пользователя не существует");
        }
    }

    public Collection<User> getAllUsers() {
        String sql = "select * from USERS";
        log.info("Список пользователей:");
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    public User putFriends(int id, int friendId) {
        String sqlQuery = "select * from USERS where USERS_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        SqlRowSet filmRowsFriend = jdbcTemplate.queryForRowSet(sqlQuery, friendId);
        if (filmRows.next() && filmRowsFriend.next()) {
            String sqlQueryFriend = "insert into USERS_FRIENDS(USERS_ID, USERS_FRIEND_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryFriend,
                    id, friendId);
        } else {
            throw new NotFoundException("Такого пользователя не существует");
        }
        log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    public Collection<User> getFriends(int id) {
        String sqlQuery = "select * from USERS where USERS_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            String sqlQueryFriend = "select uf.USERS_FRIEND_ID,u.USERS_ID,u.USERS_NAME,u.USERS_LOGIN," +
                    "u.USERS_EMAIL,u.USERS_BIRTHDAY from USERS_FRIENDS AS uf LEFT JOIN USERS AS u " +
                    "on u.USERS_ID = uf.USERS_FRIEND_ID where uf.USERS_ID = ?";
            return jdbcTemplate.query(sqlQueryFriend, this::mapRowToUser, id);
        } else {
            throw new NotFoundException("Такого пользователя не существует");
        }
    }


    public Collection<User> getCommonFriends(int id, int otherId) {
        String sqlQuery = "select * from USERS where USERS_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        SqlRowSet filmRowsOther = jdbcTemplate.queryForRowSet(sqlQuery, otherId);
        if (filmRows.next() && filmRowsOther.next()) {
            String sqlQueryFriend = "select uf.USERS_FRIEND_ID,u.USERS_ID,u.USERS_NAME,u.USERS_LOGIN," +
                    "u.USERS_EMAIL,u.USERS_BIRTHDAY from USERS_FRIENDS AS uf LEFT JOIN USERS AS u on " +
                    "u.USERS_ID = uf.USERS_FRIEND_ID where uf.USERS_ID = ? AND uf.USERS_FRIEND_ID IN (select uf.USERS_FRIEND_ID " +
                    "from USERS_FRIENDS AS uf " +
                    "LEFT JOIN USERS AS u on u.USERS_ID = uf.USERS_FRIEND_ID where uf.USERS_ID = ?)";
            return jdbcTemplate.query(sqlQueryFriend, this::mapRowToUser, id, otherId);
        } else {
            throw new NotFoundException("Такого пользователя не существует");
        }
    }

    public int deleteFriend(int id, int friendId) {
        String sqlQuery = "delete from USERS_FRIENDS  where USERS_ID = ? AND USERS_FRIEND_ID=?";
        return jdbcTemplate.update(sqlQuery, id, friendId);
    }

    private void validationUser(User user) {
        if (user.getLogin().isEmpty()) {
            log.error("Ошибка в логине пользователя {}", user);
            throw new ValidationException("Логин не может быть пустым!");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Ошибка в логине пользователя {}", user);
            throw new ValidationException("Логин не может содержать пробел!");
        }
        if (user.getEmail().isEmpty()) {
            log.error("Ошибка в email пользователя {}", user);
            throw new ValidationException("Email не может быть пустым!");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Ошибка в email пользователя {}", user);
            throw new ValidationException("Email должен содержать символ @!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка в дате рождения пользователя {}", user);
            throw new ValidationException("День рождения не может быть в будущем!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

}
