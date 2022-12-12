package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Mpa {
    int id;
    String name;

    public Mpa(int id,String name){
        this.id=id;
        this.name=name;
    }




}
