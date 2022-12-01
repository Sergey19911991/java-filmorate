package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
@Builder
@Data
public class User {
    private int id;
    @Email
    private String email;
    private String name;
    @NotNull
    private String login;
    @NotNull
    private LocalDate birthday;
    private Set<Integer> friends = new TreeSet<>();


}
