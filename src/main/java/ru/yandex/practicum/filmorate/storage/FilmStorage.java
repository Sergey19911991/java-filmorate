package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;

@Component
public interface FilmStorage {
    Film create(@Valid @RequestBody Film film);

    Film updateFilm(@Valid @RequestBody Film film);

    Collection<Film> findAll();

    Film getFilm(int id);
}
