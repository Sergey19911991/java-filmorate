package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film implements Comparable<Film> {
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private double duration;
    private Set<Integer> likes = new TreeSet<>();


    @Override
    public int compareTo(Film o) {
        return (this.getLikes().size() - o.getLikes().size()) * (-1);
    }
}
