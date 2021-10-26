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
import java.util.logging.Logger;

@Controller
@RequestMapping()
public class MainController {
    private final Logger LOGGER = Logger.getLogger("<------- LOG: MainController ------->");
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
        String[] name = principal.getName().split(" ");
        LOGGER.info("currentUser " + name[0]);
        return new ResponseEntity<>(userService.getUserByName(name[0]), HttpStatus.OK) ;
    }
}
