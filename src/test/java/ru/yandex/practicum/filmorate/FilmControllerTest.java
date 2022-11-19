package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
   private final FilmDbStorage filmDbStorage;



    @ParameterizedTest
    @CsvSource({"Film,-60, Film,1990, 12, 3,1,1",
            "Film,60,Film,1800,12,3,1,1", "Film,60,Film,1990,12,3,200,1"})
    public void errorDurationReleaseDateDescription(String name, int duration, String description,
                                                    int year, int month, int day, int repetition) {
       final Mpa mpa =  Mpa.builder().id(1).name(null).build();
        assertThrows(
                ValidationException.class, () -> {
                    {
                        filmDbStorage.create(Film.builder()
                                .name(name)
                                .duration(duration)
                                        .releaseDate(LocalDate.of(year,month,day))
                                        .description(description.repeat(repetition))
                                        .mpa(mpa)
                                .build());
                    }
                });

    }

    @Test
    public void errorName() {
        final Mpa mpa =  Mpa.builder().id(1).name(null).build();
        assertThrows(
                ValidationException.class, () -> {
                    {
                        filmDbStorage.create(Film.builder()
                                .name("")
                                .duration(60)
                                .releaseDate(LocalDate.of(1991,06,06))
                                .description("Фильм".repeat(1))
                                .mpa(mpa)
                                .build());
                    }
                });
    }


}
