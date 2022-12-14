package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;

@Slf4j
@RestController
public class FilmController {

    private final FilmDbService filmDbService;

    @Autowired
    public FilmController(FilmDbService filmDbService) {
        this.filmDbService = filmDbService;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmDbService.create(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmDbService.updateFilm(film);
    }


    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmDbService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmDbService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLikes(@PathVariable int id, @PathVariable int userId) {
        return filmDbService.putLikes(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public int deletLikes(@PathVariable int id, @PathVariable int userId) {
        return filmDbService.deletLikes(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getLikesFilms(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmDbService.getLikesFilms(count);
    }





}
