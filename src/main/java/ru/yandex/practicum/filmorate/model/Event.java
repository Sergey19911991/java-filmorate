package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private int id;
    private int userId;
    private int entityId;
    private String eventType;
    private String operation;
    private Instant timestamp = Instant.now();

    public Event(int userId, String eventType, String operation, int entityId) {
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("USERS_ID", userId);
        mapEvent.put("TIME_STAMP", timestamp);
        mapEvent.put("EVENT_TYPE", eventType);
        mapEvent.put("OPERATION", operation);
        mapEvent.put("ENTITY_ID", entityId);
        return mapEvent;
    }
}
