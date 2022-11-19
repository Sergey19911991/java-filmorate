package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
@RestController
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, FilmDbStorage filmDbStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.filmDbStorage = filmDbStorage;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmDbStorage.create(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmDbStorage.updateFilm(film);
    }


    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmDbStorage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmDbStorage.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLikes(@PathVariable int id, @PathVariable int userId) {
        return filmDbStorage.putLikes(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public int deletLikes(@PathVariable int id, @PathVariable int userId) {
        return filmDbStorage.deletLikes(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getLikesFilms(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmDbStorage.getLikesFilms(count);
    }

    @GetMapping("/genres")
    public Collection<Genre> getAllGenre() {
        return filmDbStorage.getAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        return filmDbStorage.getGenre(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> getAllRating() {
        return filmDbStorage.getAllRating();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getRating(@PathVariable int id) {
        return filmDbStorage.getRating(id);
    }

}
