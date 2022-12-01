package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@Service
public class FilmDbService {

    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmDbService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public Film create(@Valid Film film) {
        return filmDbStorage.create(film);
    }

    public Film updateFilm(@Valid Film film) {
        return filmDbStorage.updateFilm(film);
    }


    public Collection<Film> findAll() {
        return filmDbStorage.findAll();
    }


    public Film getFilm(int id) {
        return filmDbStorage.getFilm(id);
    }

    public Film putLikes(int id, int userId) {
        return filmDbStorage.putLikes(id, userId);
    }

    public int deletLikes(int id, int userId) {
        return filmDbStorage.deletLikes(id, userId);
    }

    public List<Film> getLikesFilms(Integer count) {
        return filmDbStorage.getLikesFilms(count);
    }







}