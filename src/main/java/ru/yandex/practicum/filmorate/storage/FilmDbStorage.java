package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
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


    public List<Film> getMostPopularByYearAndGenre(Integer count, Integer genreId, Integer year) {
        return filmDao.getMostPopularByYearAndGenre(count, genreId, year);
    }


    public Director creatDirector (@Valid Director director){
        return filmDao.creatDirector(director);
    }

    public Collection<Director> getAllDirector() {
        return filmDao.getAllDirector();
    }

    public Director findDirectorById(int id) {
        return filmDao.findDirectorById(id);
    }

    public int deletDirector(int id) {
        return filmDao.deletDirector(id);
    }

    public Director updateDirector(@Valid Director director) {
        return filmDao.updateDirector(director);
    }

    public List<Film> getYearFilm(int id) {
        return filmDao.getYearFilm(id);
    }

    public List<Film> getLikesFilmDirector(int id) {
        return filmDao.getLikesFilmDirector(id);
    }

    public List<Film> getLikesFilmsString(String title) {
        return filmDao.getLikesFilmsString(title);
    }

    public List<Film> getLikesFilmsDirector(String director) {
        return filmDao.getLikesFilmsDirector(director);
    }

    public List<Film> getLikesFilmsDirectorName(String directorName) {
        return filmDao.getLikesFilmsDirectorName(directorName);
    }

    public List<Film> getCommonFilm (int userId, int friendId){
        return filmDao.getCommonFilm(userId,friendId);
    }
}
