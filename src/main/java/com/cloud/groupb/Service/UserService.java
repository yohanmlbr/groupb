package com.cloud.groupb.Service;

import com.cloud.groupb.Entity.Position;
import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Entity.UserDB;
import com.cloud.groupb.Exception.ExceptionRessource;
import com.cloud.groupb.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private List<UserDB> getUsersDB() {
        return userRepository.findAll();
    }

    public List<User> getUsers() {
        List<User> list = new ArrayList<>();
        for (UserDB u : getUsersDB()) {
            list.add(dbToJson(u));
        }
        return list;
    }

    public void putUsers(List<User> users) {
        userRepository.deleteAll();
        List<UserDB> list = new ArrayList<>();
        for (User u : users) {
            list.add(jsonToDb(u));
        }
        userRepository.saveAll(list);
    }

    public void deleteUsers() {
        userRepository.deleteAll();
    }

    public User getUserById(int id) {
        return dbToJson(userRepository.findById(id).orElseThrow(
                () -> new ExceptionRessource("User", "id", id)
        ));
    }

    public User postUser(User user) {
        return dbToJson(userRepository.save(jsonToDb(user)));
    }

    public User putUserById(int id, User user) {
        UserDB udb = userRepository.findById(id).orElseThrow(
                () -> new ExceptionRessource("User", "id", id)
        );
        UserDB u = jsonToDb(user);
        return dbToJson(userRepository.save(applyModification(udb,u)));
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    private User dbToJson(UserDB userdb) {
        User user = new User();
        Position position = new Position();
        user.setId(userdb.getId());
        user.setFirstName(userdb.getFirstName());
        user.setLastName(userdb.getLastName());
        user.setBirthDay(userdb.getBirthDay());
        position.setLat(userdb.getLat());
        position.setLon(userdb.getLon());
        user.setPosition(position);
        return user;
    }

    private UserDB jsonToDb(User user) {
        UserDB userdb = new UserDB();
        userdb.setId(user.getId());
        userdb.setFirstName(user.getFirstName());
        userdb.setLastName(user.getLastName());
        userdb.setBirthDay(user.getBirthDay());
        if(user.getPosition()!=null){
            userdb.setLat(user.getPosition().getLat());
            userdb.setLon(user.getPosition().getLon());
        }
        return userdb;
    }

    private UserDB applyModification(UserDB udb, UserDB u) {
        if (u.getFirstName() != null) udb.setFirstName(u.getFirstName());
        if (u.getLastName() != null) udb.setLastName(u.getLastName());
        if (u.getBirthDay() != null) udb.setBirthDay(u.getBirthDay());
        if (u.getLat() != 0) udb.setLat(u.getLat());
        if (u.getLon() != 0) udb.setLon(u.getLon());
        return udb;
    }
}