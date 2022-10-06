package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import ru.yandex.practicum.filmorate.model.Film;

@RestController
public class FilmController {
    private List<Film> films = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping(value = "/film")
    public Film create(@RequestBody Film film) {
        LocalDate filmYear = LocalDate.of(1895, 12, 28);
        try {
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
            int k = 0;
            if (!films.isEmpty()) {
                for (int i = 0; i < films.size(); i++) {
                    if (film.getId() == films.get(i).getId()) {
                        films.remove(i);
                        films.add(film);
                        log.info("Добавлен фильм {}", film);
                        k++;
                        break;
                    }
                }
                if (k == 0) {
                    films.add(film);
                    log.info("Добавлен фильм {}", film);
                }
            } else {
                films.add(film);
                log.info("Добавлен фильм {}", film);
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return film;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return films;
    }

}
