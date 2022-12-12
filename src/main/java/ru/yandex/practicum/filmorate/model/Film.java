package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film implements Comparable<Film> {
    int id;
    @NotNull
    String name;
    @NotNull
    String description;
    @NotNull
    LocalDate releaseDate;
    @Positive
    double duration;
    Set<Integer> likes;
    LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    @NotNull
    Mpa mpa;
    Set<Director> directors;


    @Override
    public int compareTo(Film o) {
        return (this.getLikes().size() - o.getLikes().size()) * (-1);
    }
}

