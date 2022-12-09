package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import javax.validation.Valid;
import java.util.Collection;

@Service
public class DirectorService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public DirectorService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public Director creatDirector (@Valid Director director){
        return filmDbStorage.creatDirector(director);
    }

    public Collection<Director> getAllDirector() {
        return filmDbStorage.getAllDirector();
    }

    public Director findDirectorById(int id) {
        return filmDbStorage.findDirectorById(id);
    }

    public int deletDirector(int id) {
        return filmDbStorage.deletDirector(id);
    }

    public Director updateDirector(@Valid Director director) {
        return filmDbStorage.updateDirector(director);
    }

}
