package ru.yandex.practicum.filmorate.model;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Positive;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Director {
    @Positive
    int id;
    String name;
}
