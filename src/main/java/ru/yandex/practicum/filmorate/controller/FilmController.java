package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import java.util.ArrayList;
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
    public List<Film> getLikesFilms(@RequestParam(value = "count", defaultValue = "10") Integer count,
                                    @RequestParam(value = "genreId", required = false) Integer genreId,
                                    @RequestParam(value = "year", required = false) Integer year) {

        if (genreId != null || year != null) {
            return filmDbService.getMostPopularByYearAndGenre(count, genreId, year);
        } else {
            return filmDbService.getLikesFilms(count);
        }
    }
    @GetMapping("/films/director/{directorId}")
    public List<Film> getYearFilm(@PathVariable int directorId,@RequestParam String sortBy) {
        List<Film> list = new ArrayList<>();
        if(sortBy.equals("year")) {
            list.addAll(filmDbService.getYearFilm(directorId));
        }
        if(sortBy.equals("likes")) {
            list.addAll(filmDbService.getLikesFilmDirector(directorId));
        }
        return list;
    }


    @GetMapping("/films/search")
     public List<Film> getSearch(@RequestParam String query,@RequestParam String by){
        List<Film> list = new ArrayList<>();
        if(by.equals("title")) {
            list.addAll(filmDbService.getLikesFilmsString(query));
        }
        if(by.equals("director")) {
            list.addAll(filmDbService.getLikesFilmsDirector(query));
        }
        if (by.equals("title,director")||by.equals("director,title")){
            list.addAll(filmDbService.getLikesFilmsDirectorName(query));
        }
        return list;
    }
    @GetMapping("/films/common")
    public List<Film> getCommonFilm (@RequestParam int userId,@RequestParam int friendId){
             return  filmDbService.getCommonFilm(userId,friendId);
    }

}
