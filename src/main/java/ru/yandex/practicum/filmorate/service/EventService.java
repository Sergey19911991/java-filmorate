package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.util.Collection;

@Service
public class EventService {
    private final EventStorage eventStorage;

    @Autowired
    public EventService(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public Collection<Event> getAllEvent() {
        return eventStorage.getAllEvent();
    }

    public Collection<Event> getEventByUserId(int id) {
        return eventStorage.getEventByUserId(id);
    }

    public Event addEvent(Event event) {
        return eventStorage.addEvent(event);
    }
}
