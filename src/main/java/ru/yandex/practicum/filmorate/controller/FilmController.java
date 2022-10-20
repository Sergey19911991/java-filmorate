package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
@RestController
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }


    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }


    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmStorage.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLikes(@PathVariable int id, @PathVariable int userId) {
        return filmService.putLikes(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deletLikes(@PathVariable int id, @PathVariable int userId) {
        return filmService.deletLikes(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getLikesFilms(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmService.getLikesFilms(count);
    }

}
