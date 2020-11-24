package com.cloud.groupb.Service;

import com.cloud.groupb.Entity.Position;
import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Entity.UserDB;
import com.cloud.groupb.Exception.ExceptionRessource;
import com.cloud.groupb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    private List<UserDB> getUsersDB(int page){ return userRepository.findAll(PageRequest.of(page, 100)).toList();}

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
        for (UserDB u : getUsersDB(page)) {
            list.add(dbToJson(u));
        }
        return list;
    }

    /*
    public List<User> getUsersByAge(int page,int gt, int eq) {
        List<User> list = new ArrayList<>();
        for (UserDB u : getUsersDB(page)) {
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
    */

    public List<User> getUsersByAge(int page,int gt, int eq) {
        List<User> list = new ArrayList<>();
        Pageable pageable=PageRequest.of(page, 100);
        if(eq!=-1){
            Date[] dates=dateEq(eq);
            List<UserDB> users = userRepository.findByBirthDayBetween(dates[1],dates[0],pageable);
            for(UserDB u : users){
                list.add(dbToJson(u));
            }
        }
        else if(gt!=-1){
            Date date=dateGt(gt);
            List<UserDB> users = userRepository.findByBirthDayBefore(date,pageable);
            for(UserDB u : users){
                list.add(dbToJson(u));
            }
        }
        return list;
    }

    public List<User> getUsersByTerm(int page,String term) {
        List<User> list = new ArrayList<>();
        Pageable pageable=PageRequest.of(page, 100);
        List<UserDB> users = userRepository.findByLastNameContainsOrFirstNameContains(term,term,pageable);
        for(UserDB u : users){
            list.add(dbToJson(u));
        }
        return list;
    }

    public List<User> getUsersByLatLon(int page, double lat, double lon) {
        List<User> list = new ArrayList<>();
        Pageable pageable=PageRequest.of(page, 100);
        List<UserDB> users = userRepository.findByLatNearAndLonNear(lat,lon,pageable);
        for(UserDB u : users){
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

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        user.setBirthDay(df.format(userdb.getBirthDay()));

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
        try {
            userdb.setBirthDay(new SimpleDateFormat("MM/dd/yyyy").parse(user.getBirthDay()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(user.getPosition()!=null){
            userdb.setLat(user.getPosition().getLat());
            userdb.setLon(user.getPosition().getLon());
        }
        return userdb;
    }

    /*
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
    */

    public Date dateGt(int n){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.YEAR, -n);
        return c.getTime();
    }

    public Date[] dateEq(int n){
        Date [] dates = new Date[2];
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.YEAR, -n);
        dates[0]=c.getTime();
        c.add(Calendar.YEAR, -1);
        c.add(Calendar.DAY_OF_YEAR,1);
        dates[1]=c.getTime();
        return dates;
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