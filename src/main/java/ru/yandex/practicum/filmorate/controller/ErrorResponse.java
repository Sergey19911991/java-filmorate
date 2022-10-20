package ru.yandex.practicum.filmorate.controller;

public class ErrorResponse extends RuntimeException{

    public ErrorResponse(Throwable cause) {
        super(cause);
    }

}
