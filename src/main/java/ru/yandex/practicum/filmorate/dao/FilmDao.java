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
import ru.yandex.practicum.filmorate.service.EventService;


import javax.validation.Valid;


@Slf4j
@Component
public class FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final EventService eventService;

    public FilmDao(JdbcTemplate jdbcTemplate, EventService eventService) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventService = eventService;
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
                .directors(getDirectorSet(rs.getInt("FILMS_ID")))
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
            if (!(film.getDirectors() == null)) {
                Iterator<Director> iterator = film.getDirectors().iterator();
                while (iterator.hasNext()) {
                    String sqlQueryDirector = "insert into FILMS_DIRECTORS(FILMS_ID,DIRECTORS_ID) " +
                            "values (?, ?)";
                    jdbcTemplate.update(connection -> {
                        PreparedStatement stmt = connection.prepareStatement(sqlQueryDirector);
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
            if ((film.getDirectors() == null)||(film.getDirectors().isEmpty())) {
                String sqlQueryDirectorDelet = "DELETE FROM FILMS_DIRECTORS WHERE FILMS_ID = ?";
                jdbcTemplate.update(sqlQueryDirectorDelet, film.getId());
                } else {
                    String sqlQueryDirectorDelet = "DELETE FROM FILMS_DIRECTORS WHERE FILMS_ID = ?";
                    jdbcTemplate.update(sqlQueryDirectorDelet, film.getId());
                    Iterator<Director> iterator = film.getDirectors().iterator();
                    while (iterator.hasNext()) {
                        String sqlQueryDirector = "insert into FILMS_DIRECTORS(FILMS_ID,DIRECTORS_ID) " +
                                "values (?, ?)";
                        jdbcTemplate.update(connection -> {
                            PreparedStatement stmt = connection.prepareStatement(sqlQueryDirector);
                            stmt.setInt(1, film.getId());
                            stmt.setInt(2, iterator.next().getId());
                            return stmt;
                        });

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
            Event newEvent = eventService.addEvent(
                    new Event(userId, EventType.LIKE.toString(), EventOperation.ADD.toString(), id));
            log.info("В ленте новое событие - {}", newEvent);
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
            Event newEvent = eventService.addEvent(
                    new Event(userId, EventType.LIKE.toString(), EventOperation.REMOVE.toString(), id));
            log.info("В ленте новое событие - {}", newEvent);
            return jdbcTemplate.update(sqlQueryLikes, userId, id);
        } else {
            throw new NotFoundException("Такого пользователя и/или фильма не существует");
        }
    }


    public List<Film> getLikesFilms(Integer count) {
        String sqlQueryLikes = "SELECT f.FILMS_NAME, f.FILMS_DESCRIPTION, f.FILMS_DURATION,f.FILMS_RELEASE_DATE,f.FILMS_ID,f.RATING_ID " +
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


    private LinkedHashSet<Genre> getGenreSet(int id) {
        String sqlQuery = "select gm.GENRE_ID,gm.GENRE_NAME from GENRE_NAME AS gm LEFT JOIN FILMS_GENRE AS fg ON fg.GENRE_ID=gm.GENRE_ID where fg.FILMS_ID = ?";
        List<Genre> list = new ArrayList<Genre>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id));
        LinkedHashSet<Genre> genre = new LinkedHashSet<Genre>(list);
        return genre;
    }


    public List<Film> getMostPopularByYearAndGenre(Integer count, Integer genreId, Integer year) {
        if (genreId != null && year != null) {
            String sqlQuery = "SELECT DISTINCT * FROM FILMS AS f " +
                    "LEFT JOIN FILMS_GENRE AS fg ON f.FILMS_ID = fg.FILMS_ID " +
                    "LEFT JOIN FILMS_LIKES AS fl ON f.FILMS_ID=fl.FILMS_ID " +
                    "WHERE fg.GENRE_ID = ? AND EXTRACT(YEAR FROM f.FILMS_RELEASE_DATE) = ? " +
                    "GROUP BY f.FILMS_ID ORDER BY COUNT(fl.FILMS_ID) DESC LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, genreId, year, count);
        } else if (genreId != null) {
            String sqlQuery = "SELECT DISTINCT * FROM FILMS AS f " +
                    "LEFT JOIN FILMS_GENRE AS fg ON f.FILMS_ID = fg.FILMS_ID " +
                    "LEFT JOIN FILMS_LIKES AS fl ON f.FILMS_ID=fl.FILMS_ID " +
                    "WHERE fg.GENRE_ID = ? " +
                    "GROUP BY f.FILMS_ID ORDER BY COUNT(fl.FILMS_ID) DESC LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, genreId, count);
        } else {
            String sqlQuery = "SELECT DISTINCT * FROM FILMS AS f " +
                    "LEFT JOIN FILMS_LIKES AS fl ON f.FILMS_ID=fl.FILMS_ID " +
                    "WHERE EXTRACT(YEAR FROM f.FILMS_RELEASE_DATE) = ? " +
                    "GROUP BY f.FILMS_ID ORDER BY COUNT(fl.FILMS_ID) DESC LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, year, count);
        }
    }


    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getInt("DIRECTORS_ID"))
                .name(resultSet.getString("DIRECTORS_NAME"))
                .build();
    }

    public Director creatDirector(@Valid Director director) {
        try {
            if (director.getName().trim().isEmpty()){
                log.error("Нет имени режиссера");
                throw new ValidationException("Нет имени режиссера");
            }
                String sqlQuery = "insert into DIRECTORS(DIRECTORS_NAME) " +
                    "values (?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"DIRECTORS_ID"});
                stmt.setString(1, director.getName());
                return stmt;
            }, keyHolder);
            director.setId(keyHolder.getKey().intValue());
            log.info("Добавлен режиссер с id = {}", keyHolder.getKey().intValue());
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return director;
    }

    public Collection<Director> getAllDirector() {
        String sql = "select * from DIRECTORS";
        log.info("Список режиссеров:");
        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    public Director findDirectorById(int id) {
        String sqlQuery = "select *" +
                "from DIRECTORS " +
                "where DIRECTORS_ID = ?;";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRows.next()) {
            log.info("Найден режиссер id =  {}", id);
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToDirector, id);
        } else {
            throw new NotFoundException("Такого режиссера не существует");
        }
    }

    public int deletDirector(int id) {
        String sqlQuery = "select * from DIRECTORS where DIRECTORS_ID = ?";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRows.next()) {
           // String sqlQueryDirectorsFilm = "delete from FILMS_DIRECTORS  where DIRECTORS_ID = ?";
            String sqlQueryDirectors = "delete from FILMS_DIRECTORS  where DIRECTORS_ID = ?;"+"delete from DIRECTORS  where DIRECTORS_ID = ?;";
            log.info("Удален режиссер {}",id);
            return jdbcTemplate.update(sqlQueryDirectors, id,id);
        } else {
            throw new NotFoundException("Такого режиссера не существует");
        }
    }

    public Director updateDirector(@Valid Director director) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("select * from DIRECTORS where DIRECTORS_ID = ?", director.getId());
        if (directorRows.next()) {
            String sqlQuery = "update DIRECTORS set " +
                    "DIRECTORS_NAME = ? " +
                    "where DIRECTORS_ID = ?";
            jdbcTemplate.update(sqlQuery
                    , director.getName()
                    , director.getId());
        } else {
            throw new NotFoundException("Такого режиссера не существует");
        }
        return director;
    }

    private Set<Director> getDirectorSet(int id) {
        String sqlQuery = "select dir.DIRECTORS_ID, dir.DIRECTORS_NAME from DIRECTORS AS dir LEFT JOIN FILMS_DIRECTORS AS f ON f.DIRECTORS_ID=dir.DIRECTORS_ID where f.FILMS_ID = ?";
        List<Director> list = new ArrayList<Director>(jdbcTemplate.query(sqlQuery, this::mapRowToDirector, id));
        Set<Director> directors = new HashSet<Director>(list);
        return directors ;
    }

    public List<Film> getYearFilm(int id) {
        String sqlQuery = "select * from DIRECTORS where DIRECTORS_ID = ?";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRows.next()) {
        String sqlQueryYear = "SELECT f.FILMS_NAME, f.FILMS_DESCRIPTION, f.FILMS_DURATION,f.FILMS_RELEASE_DATE,f.FILMS_ID,f.RATING_ID " +
                "from FILMS AS f LEFT JOIN FILMS_DIRECTORS AS fd " +
                "on f.FILMS_ID = fd.FILMS_ID WHERE fd.DIRECTORS_ID = ? GROUP BY f.FILMS_ID ORDER BY f.FILMS_RELEASE_DATE";
        log.info("Список фильмов режиссера id = {}, отсортированный по дате выхода", id);
        return jdbcTemplate.query(sqlQueryYear, this::mapRowToFilm, id);
        } else {
            throw new NotFoundException("Такого режиссера не существует");
        }
    }

    public List<Film> getLikesFilmDirector(int id) {
        String sqlQuery = "select * from DIRECTORS where DIRECTORS_ID = ?";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRows.next()) {
        String sqlQueryYear = "SELECT f.FILMS_NAME, f.FILMS_DESCRIPTION, f.FILMS_DURATION,f.FILMS_RELEASE_DATE,f.FILMS_ID,f.RATING_ID " +
                "from FILMS AS f LEFT JOIN FILMS_DIRECTORS AS fd " +
                "on f.FILMS_ID = fd.FILMS_ID LEFT JOIN FILMS_LIKES AS fl on fl.FILMS_ID=f.FILMS_ID " +
                "WHERE fd.DIRECTORS_ID = ? GROUP BY f.FILMS_ID ORDER BY COUNT (fl.FILMS_ID)";
        log.info("Список фильмов режиссера id = {}, отсортированный по популярности", id);
        return jdbcTemplate.query(sqlQueryYear, this::mapRowToFilm, id);
        } else {
            throw new NotFoundException("Такого режиссера не существует");
        }
    }

    public List<Film> getLikesFilmsString(String title) {
        String sqlQueryLikes = "SELECT f.FILMS_NAME, f.FILMS_DESCRIPTION, f.FILMS_DURATION,f.FILMS_RELEASE_DATE,f.FILMS_ID,f.RATING_ID " +
                "from FILMS AS f LEFT JOIN FILMS_LIKES AS fl " +
                "on f.FILMS_ID = fl.FILMS_ID WHERE lower(f.FILMS_NAME) LIKE lower('%' || ? || '%')  GROUP BY f.FILMS_ID ORDER BY COUNT (fl.FILMS_ID) DESC";

        log.info("Список фильмов по популярности, содержащих в названии {}", title);
        return jdbcTemplate.query(sqlQueryLikes, this::mapRowToFilm, title);
    }

    public List<Film> getLikesFilmsDirector(String director) {
        String sqlQueryLikes = "SELECT f.FILMS_NAME, f.FILMS_DESCRIPTION, f.FILMS_DURATION,f.FILMS_RELEASE_DATE,f.FILMS_ID,f.RATING_ID " +
                "from FILMS AS f LEFT JOIN FILMS_LIKES AS fl " +
                "on f.FILMS_ID = fl.FILMS_ID LEFT JOIN FILMS_DIRECTORS AS fd on fd.FILMS_ID=f.FILMS_ID  LEFT JOIN DIRECTORS AS dr on dr.DIRECTORS_ID=fd.DIRECTORS_ID  " +
                "WHERE lower(dr.DIRECTORS_NAME) LIKE lower('%' || ? || '%')  GROUP BY f.FILMS_ID ORDER BY COUNT (fl.FILMS_ID) DESC";
        log.info("Список фильмов по популярности, в имени режиссера которых содежиться {}", director);
        return jdbcTemplate.query(sqlQueryLikes, this::mapRowToFilm, director);
    }

    public List<Film> getLikesFilmsDirectorName(String directorName) {
        String sqlQueryLikes = "SELECT f.FILMS_NAME, f.FILMS_DESCRIPTION, f.FILMS_DURATION,f.FILMS_RELEASE_DATE,f.FILMS_ID,f.RATING_ID " +
                "from FILMS AS f LEFT JOIN FILMS_LIKES AS fl " +
                "on f.FILMS_ID = fl.FILMS_ID LEFT JOIN FILMS_DIRECTORS AS fd on fd.FILMS_ID=f.FILMS_ID  LEFT JOIN DIRECTORS AS dr on dr.DIRECTORS_ID=fd.DIRECTORS_ID  " +
                "WHERE lower(dr.DIRECTORS_NAME) LIKE lower('%' || ? || '%') OR lower(f.FILMS_NAME) LIKE lower('%' || ? || '%')  GROUP BY f.FILMS_ID ORDER BY COUNT (fl.FILMS_ID) DESC";
        log.info("Список фильмов по популярности, в имени режиссера которых содежиться {}", directorName);
        return jdbcTemplate.query(sqlQueryLikes, this::mapRowToFilm, directorName,directorName);
    }

    public List<Film> getCommonFilm (int userId, int friendId){
        String sqlQuery ="SELECT DISTINCT f.RATING_ID,f.FILMS_DESCRIPTION,f.FILMS_DURATION,f.FILMS_NAME,f.FILMS_RELEASE_DATE,fl.FILMS_ID FROM " +
                 "(SELECT DISTINCT ff.FILMS_ID,ff.RATING_ID,ff.FILMS_DESCRIPTION,ff.FILMS_DURATION,ff.FILMS_NAME,ff.FILMS_RELEASE_DATE FROM FILMS AS ff LEFT JOIN FILMS_LIKES AS fl ON ff.FILMS_ID = fl.FILMS_ID WHERE fl.USERS_ID = ? )"  +
                " AS f "+
       " LEFT JOIN FILMS_LIKES AS fl ON f.FILMS_ID = fl.FILMS_ID "+
       " WHERE fl.USERS_ID = ? GROUP BY f.FILMS_ID ORDER BY COUNT (fl.FILMS_ID) DESC";
      return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, userId, friendId);
    }


}
