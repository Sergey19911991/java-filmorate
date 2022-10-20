package ru.yandex.practicum.filmorate.service;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film putLikes(int id, int userId) {
        filmStorage.getFilm(id).getLikes().add(userId);
        return filmStorage.getFilm(id);
    }

    public Film deletLikes(int id, int userId) {
        if (!filmStorage.getFilm(id).getLikes().contains(userId)) {
            log.error("У фильма {} неn дайка от пользователя {}", id, userId);
            throw new NotFoundException("Такого фильма не существует");
        }
        filmStorage.getFilm(id).getLikes().remove(userId);
        return filmStorage.getFilm(id);
    }

    public List<Film> getLikesFilms(Integer count) {
        ArrayList<Film> filmsLikes = new ArrayList<>();
        filmsLikes.addAll(filmStorage.findAll());
        return filmsLikes.stream().sorted((p0, p1) -> {
            int comp = p0.compareTo(p1);
            return comp;
        }).limit(count).collect(Collectors.toList());
    }

}
