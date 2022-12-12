package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
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


    public List<Film> getMostPopularByYearAndGenre(Integer count, Integer genreId, Integer year) {
        return filmDbStorage.getMostPopularByYearAndGenre(count, genreId, year);
    }

    public List<Film> getYearFilm(int id) {
        return filmDbStorage.getYearFilm(id);
    }

    public List<Film> getLikesFilmDirector(int id) {
        return filmDbStorage.getLikesFilmDirector(id);

    }

    public List<Film> getLikesFilmsString(String title) {
        return filmDbStorage.getLikesFilmsString(title);
    }

    public List<Film> getLikesFilmsDirector(String director) {
        return filmDbStorage.getLikesFilmsDirector(director);
    }

    public List<Film> getLikesFilmsDirectorName(String directorName) {
        return filmDbStorage.getLikesFilmsDirectorName(directorName);
    }


    public List<Film> getCommonFilm (int userId, int friendId) {
        return filmDbStorage.getCommonFilm(userId, friendId);
    }
    public void deleteFilmById(int filmId) {
        //проверим, что такое фильм есть...
        Film film = getFilm(filmId);
        filmDbStorage.deleteFilmById(filmId);

    }

}