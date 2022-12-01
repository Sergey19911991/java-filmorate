package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.util.Collection;

@Service
public class MpaService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public MpaService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }
    public Collection<Mpa> getAllRating() {
        return filmDbStorage.getAllRating();
    }

    public Mpa getRating(int id) {
        return filmDbStorage.getRating(id);
    }
}
