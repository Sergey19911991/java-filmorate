package ru.yandex.practicum.filmorate.model;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;

@Builder
@Data
public class Director {
    @Positive
    private int id;
    private String name;
}
