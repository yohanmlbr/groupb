package com.cloud.groupb.Controller;

import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Exception.InvalidEntryException;
import com.cloud.groupb.Exception.NotFoundException;
import com.cloud.groupb.Exception.RessourceException;
import com.cloud.groupb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controleur pour les services /user
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService us;

    @Autowired
    public UserController(UserService userService) {
        this.us = userService;
    }

    /**
     * Retourne tous les utilisateurs par page de 100
     * @param page
     * @return
     */
    @GetMapping
    public List<User> getUsers(@RequestParam(defaultValue = "0") int page){
        return us.getUsers(page);
    }

    /**
     * Si eq est spécifié, retournes les utilsateurs de cet âge, sinon, si gt est spécifié,
     * retourne les utilisateurs plus agé, les utilisateurs sont renvoyés par page de 100
     * @param page
     * @param gt
     * @param eq
     * @return
     * @throws InvalidEntryException
     */
    @GetMapping("/age")
    public List<User> getUsersByAge(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "-1") int gt,@RequestParam(defaultValue = "-1") int eq) throws InvalidEntryException{
        return us.getUsersByAge(page,gt,eq);
    }

    /**
     * Recherche des utilisateurs par nom et prénom par page de 100
     * @param page
     * @param term
     * @return
     */
    @GetMapping("/search")
    public List<User> getUsersByTerm(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") String term){
        return us.getUsersBySearch(page,term);
    }

    /**
     * Retourne les utilisateurs les plus proches de la position spécifiée par page de 10
     * @param page
     * @param lat
     * @param lon
     * @return
     */
    @GetMapping("/nearest")
    public List<User> getUsersByLatLon(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") double lat, @RequestParam(defaultValue = "") double lon){
        return us.getUsersByNearest(page,lat,lon);
    }

    /**
     * Remplace la liste des utilisateurs par celle passée en paramètre
     * @param users
     * @return
     */
    @PutMapping
    public ResponseEntity<List<User>> putUsers(@RequestBody List<User> users){
        return new ResponseEntity<>(us.putUsers(users), HttpStatus.CREATED);
    }

    /**
     * Supprime les utilisateurs
     */
    @DeleteMapping
    public void deleteUsers(){
        us.deleteUsers();
    }

    /**
     * Retourne l'utilisateur dont l'id est spécifié
     * @param id
     * @return
     * @throws RessourceException
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) throws RessourceException {
        return us.getUserById(id);
    }

    /**
     * Ajoute un utilisateur
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user){
        return new ResponseEntity<>(us.postUser(user), HttpStatus.CREATED);
    }

    /**
     * Met à jour l'utilisateur dont l'id est spécifié
     * @param id
     * @param user
     * @return
     * @throws RessourceException
     */
    @PutMapping("/{id}")
    public User putUserById(@PathVariable int id, @RequestBody User user) throws RessourceException {
        return us.putUserById(id, user);
    }

    /**
     * Supprime l'utilisateur dont l'id est spécifié
     * @param id
     * @throws RessourceException
     */
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable String id){
        us.deleteUserById(id);
    }

    /**
     * Retourne une erreur http pour service non trouvé
     * @param re
     * @return
     */
    @ExceptionHandler(RessourceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity notFoundException(RessourceException re) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    /**
     * Retourne une erreur http pour entré invalide
     * @param iee
     * @return
     */
    @ExceptionHandler(InvalidEntryException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity invalidEntryException(InvalidEntryException iee) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
