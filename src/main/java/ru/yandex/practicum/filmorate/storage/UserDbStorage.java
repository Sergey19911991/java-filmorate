package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


@Primary
@Component
public class UserDbStorage implements UserStorage{

    private UserDao userDao;

    public UserDbStorage (UserDao userDao){
        this.userDao=userDao;
    }

    @Override
    public User create(User user) {
        return userDao.creatUserId(user);
    }

    @Override
    public User updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public Collection<User> findAll() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userDao.findUserById(id);
    }

    public User putFriends(int id, int friendId){
        return userDao.putFriends(id,friendId);
    }

    public Collection<User> getFriends(int id) {
        return userDao.getFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        return userDao.getCommonFriends(id,otherId);
    }

    public int deletFriends(@PathVariable int id, @PathVariable int friendId) {
        return userDao.deleteFriend(id, friendId);
    }

}
