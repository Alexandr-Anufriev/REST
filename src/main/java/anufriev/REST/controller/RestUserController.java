package anufriev.REST.controller;

import anufriev.REST.model.User;
import anufriev.REST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class RestUserController {

    private final UserService userService;

    @Autowired
    public RestUserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/currentUser")
//    public User currentUser(Principal principal) {
//        return userService.getUserByName(principal.getName());
//    }
}
