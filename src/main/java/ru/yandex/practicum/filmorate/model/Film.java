package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
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
    private Set<Integer> likes;
    //private Set<Genre> genres;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    @NotNull
    private Mpa mpa;
    private Set<Director> directors;


    @Override
    public int compareTo(Film o) {
        return (this.getLikes().size() - o.getLikes().size()) * (-1);
    }
}

