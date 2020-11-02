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
}
