package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.util.Collection;

@Service
public class GenreService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public GenreService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public Collection<Genre> getAllGenre() {
        return filmDbStorage.getAllGenre();
    }

    public Genre getGenre(int id) {
        return filmDbStorage.getGenre(id);
    }
}
