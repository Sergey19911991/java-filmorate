package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ErrorResponse;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int filmNumber = 1;


    public Film create(@Valid Film film) {
        try {
            film.setId(filmNumber);
            ValidationException.validationFilm(film);
            films.put(filmNumber, film);
            filmNumber = filmNumber + 1;
            log.info("Добавлен фильм {}", film);
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return film;
    }

    public Film updateFilm(@Valid Film film) {
        try {
            if (films.containsKey(film.getId())) {
                ValidationException.validationFilm(film);
                films.put(film.getId(), film);
                log.info("Изменен фильм {}", film);
            } else {
                log.error("Фильм {} не существует", film);
                throw new NotFoundException("Такого фильма не существует");
            }
        } catch (ErrorResponse e) {
            throw new ErrorResponse(e);
        }
        return film;
    }

    public Collection<Film> findAll() {
        log.info("Список фильмов");
        return films.values();
    }

    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            log.error("Фильм {} не существует", films.get(id));
            throw new NotFoundException("Такого фильма не существует");
        }
        log.info("Фильм id = {}", id);
        return films.get(id);
    }

    @Override
    public void deleteFilmById(int filmId) {
        log.info("Удаление фильма по его Id для InMemoryFilmStorage не реализовано!");
    }

}
