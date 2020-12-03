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

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService us;

    @Autowired
    public UserController(UserService userService) {
        this.us = userService;
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(defaultValue = "0") int page){
        return us.getUsers(page);
    }

    @GetMapping("/age")
    public List<User> getUsersByAge(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "-1") int gt,@RequestParam(defaultValue = "-1") int eq) throws InvalidEntryException{
        return us.getUsersByAge(page,gt,eq);
    }

    @GetMapping("/search")
    public List<User> getUsersByTerm(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") String term){
        return us.getUsersBySearch(page,term);
    }

    @GetMapping("/nearest")
    public List<User> getUsersByLatLon(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") double lat, @RequestParam(defaultValue = "") double lon){
        return us.getUsersByNearest(page,lat,lon);
    }

    @PutMapping
    public ResponseEntity<List<User>> putUsers(@RequestBody List<User> users){
        return new ResponseEntity<>(us.putUsers(users), HttpStatus.CREATED);
    }

    @DeleteMapping
    public void deleteUsers(){
        us.deleteUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) throws RessourceException {
        return us.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user){
        return new ResponseEntity<>(us.postUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public User putUserById(@PathVariable int id, @RequestBody User user) throws RessourceException {
        return us.putUserById(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable String id) throws RessourceException {
        us.deleteUserById(id);
    }

    @ExceptionHandler(RessourceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity notFoundException(RessourceException re) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEntryException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity invalidEntryException(InvalidEntryException iee) {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
