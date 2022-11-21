package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.Collection;

@Service
public class UserDbService {
    private final UserDbStorage userDbStorage;

    @Autowired
    public UserDbService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }



    public User create(User user) {
        return userDbStorage.create(user);
    }


    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }


    public Collection<User> findAll() {
        return userDbStorage.findAll();
    }


    public User getUser(int id) {
        return userDbStorage.getUser(id);
    }

    public User putFriends(int id, int friendId){
        return userDbStorage.putFriends(id,friendId);
    }

    public Collection<User> getFriends(int id) {
        return userDbStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return userDbStorage.getCommonFriends(id,otherId);
    }

    public int deletFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDbStorage.deletFriends(id, friendId);
    }
}
