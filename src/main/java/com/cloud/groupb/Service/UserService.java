package com.cloud.groupb.Service;

import com.cloud.groupb.Entity.Position;
import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Entity.UserDB;
import com.cloud.groupb.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public List<UserDB> getUsersDB(){
        return userRepository.findAll();
    }

    public List<User> getUsers(){
        List<User> list=new ArrayList<>();
        for(UserDB u : getUsersDB()){
            list.add(dbToJson(u));
        }
        return list;
    }

    public void putUsers(List<User> users){
        userRepository.deleteAll();
        List<UserDB> list=new ArrayList<>();
        for(User u : users){
            list.add(jsonToDb(u));
        }
        userRepository.saveAll(list);
    }

    public void deleteUsers(){
        userRepository.deleteAll();
    }

    private User dbToJson(UserDB userdb){
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

    private UserDB jsonToDb(User user){
        UserDB userdb = new  UserDB();
        userdb.setId(user.getId());
        userdb.setFirstName(user.getFirstName());
        userdb.setLastName(user.getLastName());
        userdb.setBirthDay(user.getBirthDay());
        userdb.setLat(user.getPosition().getLat());
        userdb.setLon(user.getPosition().getLon());
        return userdb;
    }
}
