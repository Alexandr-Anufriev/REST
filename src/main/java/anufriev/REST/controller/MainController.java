package anufriev.REST.controller;

import anufriev.REST.model.User;
import anufriev.REST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping()
public class MainController {
    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String show() {
        return "admin";
    }

    @GetMapping("/user")
    public String showUser() {
        return "user";
    }

    @GetMapping("/currentUser")
    @ResponseBody
    public ResponseEntity<User> currentUser(Principal principal) {
        return new ResponseEntity<>(userService.getUserByName(principal.getName()), HttpStatus.OK) ;
    }
}
