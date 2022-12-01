package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exeption.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.jdbc.core.JdbcTemplate;


import javax.validation.Valid;


@Slf4j
@Component
public class FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("FILMS_ID"))
                .name(rs.getString("FILMS_NAME"))
                .description(rs.getString("FILMS_DESCRIPTION"))
                .duration(rs.getDouble("FILMS_DURATION"))
                .releaseDate(rs.getDate("FILMS_RELEASE_DATE").toLocalDate())
                .mpa(getRating(rs.getInt("RATING_ID")))
                .genres(getGenreSet(rs.getInt("FILMS_ID")))
                .build();
    }


    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }

    private Mpa mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("RATING_ID"))
                .name(resultSet.getString("RATING_NAME"))
                .build();
    }


    public Film findFilmById(Integer id) {
        String sqlQuery = "select FILMS_NAME,FILMS_ID,FILMS_DESCRIPTION," +
                "FILMS_DURATION,FILMS_RELEASE_DATE,RATING_ID from FILMS " +
                "where FILMS_ID = ?;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            log.info("Найден фильм id =  {}", id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } else {
            throw new NotFoundException("Такого фильма не существует");
        }
    }

    public Collection<Film> getAllFilms() {
        String sql = "select * from FILMS";
        log.info("Список фильмов:");
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    public Film creatFilmId(@Valid Film film) {
        try {
            ValidationException.validationFilm(film);
            String sqlQuery = "insert into FILMS(FILMS_NAME,FILMS_DESCRIPTION,FILMS_DURATION,FILMS_RELEASE_DATE,RATING_ID) " +
                    "values (?, ?, ?, ?,?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILMS_ID"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDouble(3, film.getDuration());
                stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(5, film.getMpa().getId());
                return stmt;
            }, keyHolder);
            film.setId(keyHolder.getKey().intValue());

            if (!(film.getGenres() == null)) {
                log.info("[eq");
                Iterator<Genre> iterator = film.getGenres().iterator();
                while (iterator.hasNext()) {
                    String sqlQueryGenre = "insert into FILMS_GENRE(FILMS_ID,GENRE_ID) " +
                            "values (?, ?)";
                    jdbcTemplate.update(connection -> {
                        PreparedStatement stmt = connection.prepareStatement(sqlQueryGenre);
                        stmt.setInt(1, film.getId());
                        stmt.setInt(2, iterator.next().getId());
                        return stmt;
                    });
                }
            }
            log.info("Добавлен фильм с id = {}", keyHolder.getKey().intValue());
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return film;
    }

    public Film updateFilm(@Valid Film film) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS where FILMS_ID = ?", film.getId());
        if (filmRows.next()) {
            ValidationException.validationFilm(film);
            String sqlQuery = "update FILMS set " +
                    "FILMS_NAME = ?, FILMS_DESCRIPTION = ?, FILMS_DURATION = ?, FILMS_RELEASE_DATE = ?,RATING_ID=? " +
                    "where FILMS_ID = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getDuration()
                    , film.getReleaseDate()
                    , film.getMpa().getId()
                    , film.getId());
            if (!(film.getGenres() == null)) {
                if (film.getGenres().isEmpty()) {
                    String sqlQueryGenreDelet = "DELETE FROM FILMS_GENRE WHERE FILMS_ID = ?";
                    jdbcTemplate.update(sqlQueryGenreDelet, film.getId());
                } else {
                    String sqlQueryGenreDelet = "DELETE FROM FILMS_GENRE WHERE FILMS_ID = ?";
                    jdbcTemplate.update(sqlQueryGenreDelet, film.getId());
                    Iterator<Genre> iterator = film.getGenres().iterator();
                    while (iterator.hasNext()) {
                        String sqlQueryGenre = "insert into FILMS_GENRE(FILMS_ID,GENRE_ID) " +
                                "values (?, ?)";
                        jdbcTemplate.update(connection -> {
                            PreparedStatement stmt = connection.prepareStatement(sqlQueryGenre);
                            stmt.setInt(1, film.getId());
                            stmt.setInt(2, iterator.next().getId());
                            return stmt;
                        });

                    }
                }
            }
        } else {
            throw new NotFoundException("Такого фильма не существует");
        }
        return findFilmById(film.getId());
    }

    public Film putLikes(int id, int userId) {
        String sqlQuery = "select * from FILMS where FILMS_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        String sqlQueryUser = "select * from USERS where USERS_ID = ?";
        SqlRowSet filmRowsUser = jdbcTemplate.queryForRowSet(sqlQueryUser, userId);
        if (filmRows.next() && filmRowsUser.next()) {
            String sqlQueryFriend = "insert into FILMS_LIKES(FILMS_ID, USERS_ID) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQueryFriend,
                    id, userId);
            log.info("Пользователь {}  {}", userId, id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } else {
            throw new NotFoundException("Такого пользователя и/или фильма не существует");
        }

    }

    public int deletLikes(int id, int userId) {
        String sqlQuery = "select * from FILMS where FILMS_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        String sqlQueryUser = "select * from USERS where USERS_ID = ?";
        SqlRowSet filmRowsUser = jdbcTemplate.queryForRowSet(sqlQueryUser, userId);
        if (filmRows.next() && filmRowsUser.next()) {
            String sqlQueryLikes = "delete from FILMS_LIKES  where USERS_ID = ? AND FILMS_ID=?";
            log.info("Пользователь {} удалил лайк у фильма {}", userId, id);
            return jdbcTemplate.update(sqlQueryLikes, userId, id);
        } else {
            throw new NotFoundException("Такого пользователя и/или фильма не существует");
        }
    }


    public List<Film> getLikesFilms(Integer count) {
        String sqlQueryLikes = "SELECT FILMS_NAME, f.FILMS_DESCRIPTION, f.FILMS_DURATION,f.FILMS_RELEASE_DATE,f.FILMS_ID,f.RATING_ID " +
                "from FILMS AS f LEFT JOIN FILMS_LIKES AS fl " +
                "on f.FILMS_ID = fl.FILMS_ID GROUP BY f.FILMS_ID ORDER BY COUNT (fl.FILMS_ID) DESC LIMIT ?";
        log.info("{} самых популярных фильмов", count);
        return jdbcTemplate.query(sqlQueryLikes, this::mapRowToFilm, count);
    }

    public Collection<Genre> getAllGenre() {
        String sqlGenre = "select * from GENRE_NAME";
        log.info("Список жанров фильмов:");
        return jdbcTemplate.query(sqlGenre, this::mapRowToGenre);
    }

    public Genre getGenre(int id) {
        String sqlQuery = "select * from GENRE_NAME where GENRE_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            log.info("Найден жанр id =  {}", id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } else {
            throw new NotFoundException("Такого жанра не существует");
        }
    }


    public Collection<Mpa> getAllRating() {
        String sqlGenre = "select * from FILMS_RATING";
        log.info("Список рейтингов MPA:");
        return jdbcTemplate.query(sqlGenre, this::mapRowToRating);
    }

    public Mpa getRating(int id) {
        String sqlQuery = "select * from FILMS_RATING where RATING_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            log.info("Найден рейтинг mpa id =  {}", id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, id);
        } else {
            throw new NotFoundException("Такого рейтинга mpa не существует");
        }
    }

    private Set<Genre> getGenreSet(int id) {
        String sqlQuery = "select gm.GENRE_ID,gm.GENRE_NAME from GENRE_NAME AS gm LEFT JOIN FILMS_GENRE AS fg ON fg.GENRE_ID=gm.GENRE_ID where fg.FILMS_ID = ?";
        List<Genre> list = new ArrayList<Genre>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id));
        Set<Genre> genre = new HashSet<Genre>(list);
        return genre;
    }

}
