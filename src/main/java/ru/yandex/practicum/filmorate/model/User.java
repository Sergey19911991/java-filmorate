package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    int id;
    @Email
    String email;
    String name;
    @NotNull
    String login;
    @NotNull
    LocalDate birthday;
    Set<Integer> friends = new TreeSet<>();


}
