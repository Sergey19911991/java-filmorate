package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;


import javax.validation.Valid;
import java.util.Collection;
@RequestMapping("/directors")
@Slf4j
@RestController
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @PostMapping(value = "")
    public Director create(@Valid @RequestBody Director director) {
        return directorService.creatDirector(director);
    }

    @GetMapping("")
    public Collection<Director> getAllDirector() {
        return directorService.getAllDirector();
    }
    @GetMapping("/{id}")
    public Director findDirectorById(@PathVariable int id) {
        return directorService.findDirectorById(id);
    }

    @DeleteMapping("/{id}")
    public int deletDirector(@PathVariable int id) {
        return directorService.deletDirector(id);
    }

    @PutMapping("")
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorService.updateDirector(director);
    }
}
