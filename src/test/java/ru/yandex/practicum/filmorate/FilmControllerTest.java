package ru.yandex.practicum.filmorate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTest {

    public InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @Test
    public void createFilm() {
        Film film = new Film();
        film.setName("Film");
        film.setDuration(60);
        film.setDescription("Film");
        film.setReleaseDate(LocalDate.of(1990, 12, 3));
        inMemoryFilmStorage.create(film);
        assertTrue(inMemoryFilmStorage.findAll().contains(film));
    }


    @Test
    public void errorName() {
        assertThrows(
                ValidationException.class, () -> {
                    {
                        Film film = new Film();
                        film.setName("");
                        film.setDuration(60);
                        film.setDescription("Film");
                        film.setReleaseDate(LocalDate.of(1990, 12, 3));
                        inMemoryFilmStorage.create(film);
                    }
                });
    }

    @ParameterizedTest
    @CsvSource({"Film,-60, Film,1990, 12, 3,1",
            "Film,60,Film,1800,12,3,1", "Film,60,Film,1990,12,3,200"})
    public void errorDurationReleaseDateDescription(String name, int duration, String description, int year, int month, int day, int repetition) {
        assertThrows(
                ValidationException.class, () -> {
                    {
                        Film film = new Film();
                        film.setName(name);
                        film.setDuration(duration);
                        film.setDescription(description.repeat(repetition));
                        film.setReleaseDate(LocalDate.of(year, month, day));
                        inMemoryFilmStorage.create(film);
                    }
                });

    }

}

