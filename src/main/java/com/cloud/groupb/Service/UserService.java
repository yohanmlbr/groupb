package com.cloud.groupb.Service;

import com.cloud.groupb.Entity.Position;
import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Entity.UserDB;
import com.cloud.groupb.Exception.ExceptionRessource;
import com.cloud.groupb.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
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

    private List<UserDB> getUsersDBByPage(int page){ return userRepository.findAll(PageRequest.of(page, 100)).toList();}

    public List<User> getTest(){
        List<User> list = new ArrayList<>();
        /*
        for (Object[] obj : userRepository.getUsersDBByPage()) {
            list.add(dbToJson(Cast(obj)));
        }
        */
        return list;
    }

    public List<User> getUsers(int page) {
        List<User> list = new ArrayList<>();
        for (UserDB u : getUsersDBByPage(page)) {
            list.add(dbToJson(u));
        }
        return list;
    }

    public List<User> getUsersByAge(int page,int gt, int eq) {
        List<User> list = new ArrayList<>();
        for (UserDB u : getUsersDBByPage(page)) {
            int age=getAge(u);
            if(eq!=-1 && gt!=-1){
                if(age==eq && age>gt){
                    list.add(dbToJson(u));
                }
            }
            else if(eq!=-1){
                if(age==eq){
                    list.add(dbToJson(u));
                }
            }
            else if(gt!=-1){
                if(age>gt){
                    list.add(dbToJson(u));
                }
            }
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

    private UserDB Cast(Object[] obj)
    {
        UserDB u = new UserDB();
        u.setId(((UserDB)obj[0]).getId());
        u.setFirstName(((UserDB)obj[0]).getFirstName());
        u.setLastName(((UserDB)obj[0]).getLastName());
        u.setBirthDay(((UserDB)obj[0]).getBirthDay());
        u.setLat(((UserDB)obj[0]).getLat());
        u.setLon(((UserDB)obj[0]).getLon());
        return u;
    }

    public int getAge(UserDB u){
        LocalDate today = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birthday = LocalDate.parse(u.getBirthDay(), dtf);
        int age=Period.between(birthday,today).getYears();
        System.out.println(u.getFirstName()+" "+u.getLastName()+" : "+age+" ans");
        return age;
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