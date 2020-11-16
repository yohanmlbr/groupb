package com.cloud.groupb.Controller;

import com.cloud.groupb.Entity.User;
import com.cloud.groupb.Repository.UserRepository;
import com.cloud.groupb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository ur;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.ur = userRepository;
    }

    @GetMapping("/")
    public List<User> getUsers(){
        UserService us = new UserService(ur);
        return us.getUsers();
    }

    @PutMapping("/")
    public void putUsers(@RequestBody List<User> users){
        UserService us = new UserService(ur);
        us.putUsers(users);
    }

    @DeleteMapping("/")
    public void deleteUsers(){
        UserService us = new UserService(ur);
        us.deleteUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
        UserService us = new UserService(ur);
        return us.getUserById(id);
    }

    @PostMapping("/")
    public User postUser(@RequestBody User user){
        UserService us = new UserService(ur);
        return us.postUser(user);
    }

    @PutMapping("/{id}")
    public User putUserById(@PathVariable int id, @RequestBody User user){
        UserService us = new UserService(ur);
        return us.putUserById(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id){
        UserService us = new UserService(ur);
        us.deleteUserById(id);
    }
}
