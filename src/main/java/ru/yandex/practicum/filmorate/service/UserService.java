package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User putFriends(int id, int friendId) {
        NotFoundExceptionUser(id);
        NotFoundExceptionUser(friendId);
        userStorage.getUser(id).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(id);
        log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
        return userStorage.getUser(id);
    }

    public User deletFriends(int id, int friendId) {
        NotFoundExceptionUser(id);
        NotFoundExceptionUser(friendId);
        userStorage.getUser(id).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(id);
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
        return userStorage.getUser(id);
    }

    public List getFriends(int id) {
        NotFoundExceptionUser(id);
        ArrayList<Integer> friendsIdUser = new ArrayList<>(userStorage.getUser(id).getFriends());
        ArrayList<User> friendsId = new ArrayList<>();
        for (int i = 0; i < friendsIdUser.size(); i++) {
            friendsId.add(userStorage.getUser(friendsIdUser.get(i)));
        }
        log.info("Список друзей пользователя {}", id);
        return friendsId;
    }

    public List getCommonFriends(int id, int otherId) {
        NotFoundExceptionUser(id);
        NotFoundExceptionUser(otherId);
        ArrayList<User> commonFriends = new ArrayList<>();
        ArrayList<Integer> idFriends = new ArrayList<>(userStorage.getUser(id).getFriends());
        ArrayList<Integer> otherFriends = new ArrayList<>(userStorage.getUser(otherId).getFriends());
        Set<Integer> commonIdFriends = findCommonElements(idFriends, otherFriends);
        for (Integer commonIdFriend : commonIdFriends) {
            commonFriends.add(userStorage.getUser(commonIdFriend));
        }
        log.info("Список общих друзей пользователей {} и {}", id, otherId);
        return commonFriends;
    }

    public void NotFoundExceptionUser(int id) {
        if (!(userStorage.findAll().contains(userStorage.getUser(id)))) {
            log.error("Пользователь {} не существует", id);
            throw new NotFoundException("Такого пользователя не существует");
        }
    }

    private static <T> Set<T> findCommonElements(List<T> first, List<T> second) {
        return first.stream().filter(second::contains).collect(Collectors.toSet());
    }

}
