package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.FilmController;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    public void createFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film");
        film.setDuration(60);
        film.setDescription("Film");
        film.setReleaseDate(LocalDate.of(1990, 12, 3));
        filmController.create(film);
        assertEquals(film, filmController.findAll().get(0));
    }


    @Test
    public void errorName() {
        Film film = new Film();
        film.setId(1);
        film.setName("");
        film.setDuration(60);
        film.setDescription("Film");
        film.setReleaseDate(LocalDate.of(1990, 12, 3));
        filmController.create(film);
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void errorDuration() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film");
        film.setDuration(-60);
        film.setDescription("Film");
        film.setReleaseDate(LocalDate.of(1990, 12, 3));
        filmController.create(film);
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void errorReleaseDate() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film");
        film.setDuration(60);
        film.setDescription("Film");
        film.setReleaseDate(LocalDate.of(1800, 12, 3));
        filmController.create(film);
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    public void errorDescription() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film");
        film.setDuration(60);
        film.setDescription("FilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilm" +
                "FilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilmFilm");
        film.setReleaseDate(LocalDate.of(1990, 12, 3));
        filmController.create(film);
        assertEquals(0, filmController.findAll().size());
    }
}

