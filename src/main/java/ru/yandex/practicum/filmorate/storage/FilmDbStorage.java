package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.dao.FilmDao;

import javax.validation.Valid;

@Primary
@Component
public class FilmDbStorage implements FilmStorage {
    private FilmDao filmDao;

    public FilmDbStorage(FilmDao filmDao) {
        this.filmDao = filmDao;
    }


    @Override
    public Film create(@Valid Film film) {
        return filmDao.creatFilmId(film);
    }

    @Override
    public Film updateFilm(@Valid Film film) {
        return filmDao.updateFilm(film);
    }

    @Override
    public Collection<Film> findAll() {
        return filmDao.getAllFilms();
    }

    @Override
    public Film getFilm(int id) {
        return filmDao.findFilmById(id);
    }

    public Film putLikes(int id, int userId) {
        return filmDao.putLikes(id, userId);
    }

    public int deletLikes(int id, int userId) {
        return filmDao.deletLikes(id, userId);
    }

    public List<Film> getLikesFilms(Integer count) {
        return filmDao.getLikesFilms(count);
    }

    public Collection<Genre> getAllGenre() {
        return filmDao.getAllGenre();
    }

    public Genre getGenre(int id) {
        return filmDao.getGenre(id);
    }

    public Collection<Mpa> getAllRating() {
        return filmDao.getAllRating();
    }

    public Mpa getRating(int id) {
        return filmDao.getRating(id);
    }

}
