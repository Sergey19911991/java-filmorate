package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

public class FilmControllerTest {
   private FilmStorage filmStorage;
   private FilmService filmService;
   private FilmController filmController = new FilmController(filmStorage,filmService);

    @Test
    public void createFilm() {
        Film film = new Film();
        film.setName("Film");
        film.setDuration(60);
        film.setDescription("Film");
        film.setReleaseDate(LocalDate.of(1990, 12, 3));
        filmController.create(film);
        assertEquals(true, filmController.findAll().contains(film));
    }


    @Test
    public void errorName() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Film film = new Film();
                        film.setName("");
                        film.setDuration(60);
                        film.setDescription("Film");
                        film.setReleaseDate(LocalDate.of(1990, 12, 3));
                        filmController.create(film);
                    }
                });
    }

    @Test
    public void errorDuration() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Film film = new Film();
                        film.setName("Film");
                        film.setDuration(-60);
                        film.setDescription("Film");
                        film.setReleaseDate(LocalDate.of(1990, 12, 3));
                        filmController.create(film);
                    }
                });

    }

    @Test
    public void errorReleaseDate() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Film film = new Film();
                        film.setId(1);
                        film.setName("Film");
                        film.setDuration(60);
                        film.setDescription("Film");
                        film.setReleaseDate(LocalDate.of(1800, 12, 3));
                        filmController.create(film);
                    }
                });
    }

    @Test
    public void errorDescription() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        Film film = new Film();
                        film.setId(1);
                        film.setName("Film");
                        film.setDuration(60);
                        film.setDescription("Film".repeat(200));
                        film.setReleaseDate(LocalDate.of(1990, 12, 3));
                        filmController.create(film);
                    }
                });
    }
}

