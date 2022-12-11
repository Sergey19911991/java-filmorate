package ru.yandex.practicum.filmorate.storage.event;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class EventDBStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    public EventDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Event> getAllEvent() {
        final String sql = "SELECT * FROM events";
        final Collection<Event> events = jdbcTemplate.query(sql, EventDBStorage::makeEvent);
        return events;
    }

    @Override
    public Collection<Event> getEventByUserId(int id) {
        final String sql = "SELECT * FROM events WHERE users_id=?";
        final Collection<Event> events = jdbcTemplate.query(sql, EventDBStorage::makeEvent, id);
        return events;
    }

    @Override
    public Event addEvent(Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("EVENTS")
                .usingGeneratedKeyColumns("EVENT_ID");
        int eventId = simpleJdbcInsert.executeAndReturnKey(event.toMap()).intValue();
        event.setId(eventId);
        return event;
    }

    static public Event makeEvent(ResultSet rs, int rowNum) throws SQLException {
        return new Event(
                rs.getInt("EVENT_ID"),
                rs.getInt("USERS_ID"),
                rs.getInt("ENTITY_ID"),
                rs.getString("EVENT_TYPE"),
                rs.getString("OPERATION"),
                rs.getTimestamp("TIME_STAMP").toInstant()
        );
    }
}
