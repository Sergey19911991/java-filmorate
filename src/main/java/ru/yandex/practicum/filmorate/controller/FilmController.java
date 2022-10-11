package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.util.Collection;
import java.time.LocalDate;
import java.util.HashMap;

import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@RestController
public class FilmController {
    private HashMap<Integer, Film> films = new HashMap<>();
    private int filmNumber = 1;

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        try {
            film.setId(filmNumber);
            validationFilm(film);
            films.put(filmNumber, film);
            filmNumber = filmNumber + 1;
            log.info("Добавлен фильм {}", film);
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        try {
            if (films.containsKey(film.getId())) {
                validationFilm(film);
                films.put(film.getId(), film);
                log.info("Добавлен пользователь {}", film);
            } else {
                log.error("Пользователь {} не существует", film);
                throw new ValidationException("Такого пользователя не существует");
            }
        } catch (ValidationException e) {
            throw new ValidationException(e);
        }
        return film;
    }


    @GetMapping("/films")
    public Collection<Film> findAll() {
        Collection<Film> filmsCollection = films.values();
        return filmsCollection;
    }

    private void validationFilm(Film film) {
        LocalDate filmYear = LocalDate.of(1895, 12, 28);
        if (film.getDuration() < 0) {
            log.error("Ошибка в продолжительности фильма {}", film);
            throw new ValidationException("Продолжительность фильма не может быть отрицательным числом!");
        }
        if (film.getDescription().length() > 200) {
            log.error("Ошибка в описании фильма {}", film);
            throw new ValidationException("Описание фильма не может содержать более 200 символов!");
        }
        if (film.getName().isEmpty()) {
            log.error("Ошибка в названии фильма {}", film);
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if (film.getReleaseDate().isBefore(filmYear)) {
            log.error("Ошибка в дате выхода фильма фильма {}", film);
            throw new ValidationException("Дата выхода фильма не может быть раньше 28 декабря 1895 года!");
        }
    }

}
