package ru.yandex.practicum.filmorate.storage.event;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

public interface EventStorage {
    Collection<Event> getAllEvent();

    Collection<Event> getEventByUserId(int id);

    Event addEvent(Event event);
}
