package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.ErrorResponse;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
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
            validationFilm(film);
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
                validationFilm(film);
                films.put(film.getId(), film);
                log.info("Добавлен пользователь {}", film);
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
        Collection<Film> filmsCollection = films.values();
        return filmsCollection;
    }

    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            log.error("Фильм {} не существует", films.get(id));
            throw new NotFoundException("Такого фильма не существует");
        }
        return films.get(id);
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
