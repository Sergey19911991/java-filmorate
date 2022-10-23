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
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film putLikes(int id, int userId) {
        NotFoundExceptionFilm(id);
        userService.NotFoundExceptionUser(userId);
        filmStorage.getFilm(id).getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, id);
        return filmStorage.getFilm(id);
    }

    public Film deletLikes(int id, int userId) {
        NotFoundExceptionFilm(id);
        userService.NotFoundExceptionUser(userId);
        if (!filmStorage.getFilm(id).getLikes().contains(userId)) {
            log.error("У фильма {} нет лайка от пользователя {}", id, userId);
            throw new NotFoundException("Такого фильма не существует");
        }
        filmStorage.getFilm(id).getLikes().remove(userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, id);
        return filmStorage.getFilm(id);
    }

    public List<Film> getLikesFilms(Integer count) {
        ArrayList<Film> filmsLikes = new ArrayList<>(filmStorage.findAll());
        log.info("{} самых популярных фильмов", count);
        return filmsLikes.stream().sorted((p0, p1) -> {
            return p0.compareTo(p1);
        }).limit(count).collect(Collectors.toList());
    }

    public void NotFoundExceptionFilm(int id) {
        if (!(filmStorage.findAll().contains(filmStorage.getFilm(id)))) {
            log.error("Фильм {} не существует", id);
            throw new NotFoundException("Такого фильма не существует");
        }
    }

}
