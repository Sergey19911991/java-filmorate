package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmDbService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @PostMapping(value = "/directors")
    public Director create(@Valid @RequestBody Director director) {
        return directorService.creatDirector(director);
    }

    @GetMapping("/directors")
    public Collection<Director> getAllDirector() {
        return directorService.getAllDirector();
    }
    @GetMapping("/directors/{id}")
    public Director findDirectorById(@PathVariable int id) {
        return directorService.findDirectorById(id);
    }

    @DeleteMapping("/directors/{id}")
    public int deletDirector(@PathVariable int id) {
        return directorService.deletDirector(id);
    }

    @PutMapping("/directors")
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }
}
