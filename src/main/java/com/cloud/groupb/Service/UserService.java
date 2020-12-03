package com.cloud.groupb.Service;

import com.cloud.groupb.Entity.Position;
import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Entity.UserDB;
import com.cloud.groupb.Exception.InvalidEntryException;
import com.cloud.groupb.Exception.RessourceException;
import com.cloud.groupb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Services utilisateurs
 */
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    /**
     * Retourne tous les utilisateurs par page de 100
     * @param page
     * @return
     */
    public List<User> getUsers(int page) {
        List<User> list = new ArrayList<>();
        List<UserDB> users = userRepository.findAll(PageRequest.of(page, 100)).toList();
        for (UserDB u : users) {
            list.add(dbToJson(u));
        }
        return list;
    }

    /**
     * Si eq est spécifié, retournes les utilsateurs de cet âge, sinon, si gt est spécifié,
     * retourne les utilisateurs plus agé, les utilisateurs sont renvoyés par page de 100
     * @param page
     * @param gt
     * @param eq
     * @return
     */
    public List<User> getUsersByAge(int page,int gt, int eq) {
        if(gt<-1 || eq <-1){
            throw new InvalidEntryException("âge négatif");
        }
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

    /**
     * Recherche des utilisateurs par nom et prénom par page de 100
     * @param page
     * @param term
     * @return
     */
    public List<User> getUsersBySearch(int page,String term) {
        List<User> list = new ArrayList<>();
        Pageable pageable=PageRequest.of(page, 100);
        List<UserDB> users = userRepository.findByLastNameContainsOrFirstNameContains(term,term,pageable);
        for(UserDB u : users){
            list.add(dbToJson(u));
        }
        return list;
    }

    /**
     * Retourne les utilisateurs les plus proches de la position spécifiée par page de 10
     * @param page
     * @param lat
     * @param lon
     * @return
     */
    public List<User> getUsersByNearest(int page, double lat, double lon) {
        List<User> list = new ArrayList<>();
        Pageable pageable=PageRequest.of(page, 10);
        List<UserDB> users = userRepository.findByLatLon(lat,lon,pageable);
        for(UserDB u : users){
            list.add(dbToJson(u));
        }
        return list;
    }

    /**
     * Remplace la liste des utilisateurs par celle passée en paramètre
     * @param users
     * @return
     */
    public List<User> putUsers(List<User> users) {
        userRepository.deleteAll();
        List<UserDB> list = new ArrayList<>();
        for (User u : users) {
            list.add(jsonToDb(u));
        }
        userRepository.saveAll(list);
        List<User> listR = new ArrayList<>();
        for (UserDB udb : list){
            listR.add(dbToJson(udb));
        }
        return listR;
    }

    /**
     * Supprime les utilisateurs
     */
    public void deleteUsers() {
        userRepository.deleteAll();
    }

    /**
     * Retourne l'utilisateur dont l'id est spécifié
     * @param id
     * @return
     */
    public User getUserById(String id) {
        int idd = -1;
        try{
            idd = Integer.parseInt(id);
        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
        }
        return dbToJson(userRepository.findById(idd).orElseThrow(
                () -> new RessourceException("User", "id", id)
        ));
    }

    /**
     * Ajoute un utilisateur
     * @param user
     * @return
     */
    public User postUser(User user) {
        return dbToJson(userRepository.save(jsonToDb(user)));
    }

    /**
     * Met à jour l'utilisateur dont l'id est spécifié
     * @param id
     * @param user
     * @return
     */
    public User putUserById(int id, User user) {
        UserDB udb = userRepository.findById(id).orElseThrow(
                () -> new RessourceException("User", "id", id)
        );
        UserDB u = jsonToDb(user);
        return dbToJson(userRepository.save(applyModification(udb,u)));
    }

    /**
     * Supprime l'utilisateur dont l'id est spécifié
     * @param id
     */
    public void deleteUserById(String id) {
        int idd = -1;
        try{
            idd = Integer.parseInt(id);
        }catch(NumberFormatException nfe){
            nfe.printStackTrace();
        }
        userRepository.deleteById(idd);
    }


    /**
     * Converti un utilisateur format base de données en format JSON
     * @param userdb
     * @return
     */
    private User dbToJson(UserDB userdb) {
        User user = new User();
        Position position = new Position();
        user.setId(String.valueOf(userdb.getId()));
        user.setFirstName(userdb.getFirstName());
        user.setLastName(userdb.getLastName());

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        user.setBirthDay(df.format(userdb.getBirthDay()));

        position.setLat(userdb.getLat());
        position.setLon(userdb.getLon());
        user.setPosition(position);
        return user;
    }

    /**
     * Converti un utilisateur format JSON en format base de données
     * @param user
     * @return
     */
    private UserDB jsonToDb(User user) {
        UserDB userdb = new UserDB();
        if(user.getId()!=null){
            userdb.setId(Integer.parseInt(user.getId()));
        }
        userdb.setFirstName(user.getFirstName());
        userdb.setLastName(user.getLastName());
        if(user.getBirthDay()!=null){
            try {
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                userdb.setBirthDay(df.parse(user.getBirthDay()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(user.getPosition()!=null){
            userdb.setLat(user.getPosition().getLat());
            userdb.setLon(user.getPosition().getLon());
        }
        return userdb;
    }

    /**
     * Retourne la date actuel moins n années
     * @param n
     * @return
     */
    private Date dateGt(int n){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        c.add(Calendar.YEAR, -n);
        return c.getTime();
    }

    /**
     * Retournes les deux dates entre lesquelles l'utilisateur doit être né pour avoir l'âge n
     * @param n
     * @return
     */
    private Date[] dateEq(int n){
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

    /**
     * Copie l'utilisateur u dans udb
     * @param udb
     * @param u
     * @return
     */
    private UserDB applyModification(UserDB udb, UserDB u) {
        if (u.getFirstName() != null) udb.setFirstName(u.getFirstName());
        if (u.getLastName() != null) udb.setLastName(u.getLastName());
        if (u.getBirthDay() != null) udb.setBirthDay(u.getBirthDay());
        if (u.getLat() != 0) udb.setLat(u.getLat());
        if (u.getLon() != 0) udb.setLon(u.getLon());
        return udb;
    }
}
