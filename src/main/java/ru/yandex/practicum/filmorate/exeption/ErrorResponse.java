package ru.yandex.practicum.filmorate.exeption;

public class ErrorResponse extends RuntimeException {

    public ErrorResponse(Throwable cause) {
        super(cause);
    }

}
