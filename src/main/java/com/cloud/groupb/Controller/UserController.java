package com.cloud.groupb.Controller;

import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/test")
    public List<User> getTest(){
        return us.getTest();
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(defaultValue = "0") int page){
        return us.getUsers(page);
    }

    @GetMapping("/age")
    public List<User> getUsersByAge(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "-1") int gt,@RequestParam(defaultValue = "-1") int eq){
        return us.getUsersByAge(page,gt,eq);
    }

    @GetMapping("/search")
    public List<User> getUsersByTerm(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") String term){
        return us.getUsersByTerm(page,term);
    }

    @GetMapping("/nearest")
    public List<User> getUsersByLatLon(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "") double lat, @RequestParam(defaultValue = "") double lon){
        return us.getUsersByLatLon(page,lat,lon);
    }

    @PutMapping
    public void putUsers(@RequestBody List<User> users){
        us.putUsers(users);
    }

    @DeleteMapping
    public void deleteUsers(){
        us.deleteUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
        return us.getUserById(id);
    }

    @PostMapping
    public User postUser(@RequestBody User user){
        return us.postUser(user);
    }

    @PutMapping("/{id}")
    public User putUserById(@PathVariable int id, @RequestBody User user){
        return us.putUserById(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id){
        us.deleteUserById(id);
    }
}
