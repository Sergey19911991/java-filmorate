package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    int eventId;
    int userId;
    int entityId;
    String eventType;
    String operation;
    Instant timestamp = Instant.now();

    public Event(int userId, String eventType, String operation, int entityId) {
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }

    @JsonGetter("timestamp")
    public long timestampToEpochMillis() { return timestamp.toEpochMilli();}

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
