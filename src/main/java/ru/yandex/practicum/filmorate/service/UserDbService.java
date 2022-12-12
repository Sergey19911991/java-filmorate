package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDbService {
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public UserDbService(UserDbStorage userDbStorage, FilmDbStorage filmDbStorage) {
        this.userDbStorage = userDbStorage;
        this.filmDbStorage = filmDbStorage;
    }


    public User create(User user) {
        return userDbStorage.create(user);
    }


    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }

    public void deleteUserById(int userId) {
        //проверим, что такой юзер есть...
        User user = getUser(userId);
        userDbStorage.deleteUserById(userId);
    }

    public Collection<User> findAll() {
        return userDbStorage.findAll();
    }


    public User getUser(int id) {
        return userDbStorage.getUser(id);
    }

    public User putFriends(int id, int friendId) {
        return userDbStorage.putFriends(id, friendId);
    }

    public Collection<User> getFriends(int id) {
        return userDbStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return userDbStorage.getCommonFriends(id, otherId);
    }

    public int deletFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDbStorage.deletFriends(id, friendId);
    }

    public List<Film> getRecommendations(int id) {
        List<Integer> userLikes = userDbStorage.getUserFavoriteFilmsIds(id);
        if(userLikes.isEmpty()) {
            /*
            throw new NotFoundException("Пользователь с id " + id + " еще не поставил ни одного лайка.");
            Казалось логичным выбросить исключение, когда пользователю нечего порекомендовать,
            но тесты выдают ошибку на исключение. Так что возвращаем пустой список...
            */
            return List.of();
        }

        Map<User, List<Integer>> otherLikes = new HashMap<>();
        findAll().forEach(e -> otherLikes.put(e, userDbStorage.getUserFavoriteFilmsIds(e.getId())));

        long matches;
        long mostMatches = 0;
        int mostMatchedUserId = 0;
        for (Map.Entry<User, List<Integer>> entry : otherLikes.entrySet()) {
            if(entry.getKey().getId() == id) {
                continue;
            }

            matches = entry.getValue().stream()
                    .filter(userLikes::contains)
                    .count();

            if (mostMatches < matches) {
                mostMatches = matches;
                mostMatchedUserId = entry.getKey().getId();
            }
        }

        return userDbStorage.getUserFavoriteFilmsIds(mostMatchedUserId).stream()
                .filter(e -> !userLikes.contains(e))
                .map(filmDbStorage::getFilm)
                .collect(Collectors.toList());
    }
}
