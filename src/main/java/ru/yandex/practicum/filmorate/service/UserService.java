package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.NotFoundException;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;


import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User putFriends(int id, int friendId) {
        if (!(userStorage.findAll().contains(userStorage.getUser(id))) || !(userStorage.findAll().contains(userStorage.getUser(friendId)))) {
            log.error("Пользователь не существует");
            throw new NotFoundException("Такого пользователя не существует");
        }
        userStorage.getUser(id).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(id);
        return userStorage.getUser(id);
    }

    public User deletFriends(int id, int friendId) {
        userStorage.getUser(id).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(id);
        return userStorage.getUser(id);
    }

    public List getFriends(int id) {
        if (!(userStorage.findAll().contains(userStorage.getUser(id)))) {
            log.error("Пользователь не существует");
            throw new NotFoundException("Такого пользователя не существует");
        }
        ArrayList<Integer> friendsIdUser = new ArrayList<>();
        friendsIdUser.addAll(userStorage.getUser(id).getFriends());
        ArrayList<User> friendsId = new ArrayList<>();
        for (int i = 0; i < friendsIdUser.size(); i++) {
            friendsId.add(userStorage.getUser(friendsIdUser.get(i)));
        }
        return friendsId;
    }

    public List getCommonFriends(int id, int otherId) {
        if (!(userStorage.findAll().contains(userStorage.getUser(id))) || !(userStorage.findAll().contains(userStorage.getUser(otherId)))) {
            log.error("Пользователь не существует");
            throw new NotFoundException("Такого пользователя не существует");
        }
        ArrayList<User> commonFriends = new ArrayList<>();
        ArrayList<Integer> idFriends = new ArrayList<>();
        ArrayList<Integer> otherFriends = new ArrayList<>();
        idFriends.addAll(userStorage.getUser(id).getFriends());
        otherFriends.addAll(userStorage.getUser(otherId).getFriends());
        for (int i = 0; i < idFriends.size(); i++) {
            for (int k = 0; k < otherFriends.size(); k++) {
                if (idFriends.get(i) == otherFriends.get(k)) {
                    commonFriends.add(userStorage.getUser(idFriends.get(i)));
                }
            }

        }
        return commonFriends;
    }


}
