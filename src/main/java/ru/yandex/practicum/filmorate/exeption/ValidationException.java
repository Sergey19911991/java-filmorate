package ru.yandex.practicum.filmorate.exeption;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
@Slf4j
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public static void validationFilm(Film film) {
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