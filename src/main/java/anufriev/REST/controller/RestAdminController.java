package anufriev.REST.controller;

import anufriev.REST.model.User;
import anufriev.REST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class RestAdminController {

    private final UserService userService;

    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> showAllUsers() {
        return userService.allUsers();
    }

    @GetMapping("/currentUser")
    public User currentUser(Principal principal) {
        System.out.println(principal);
        return userService.getUserByName(principal.getName());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addUser(@RequestBody User newUser){
        System.out.println("вызвался addUser: " + newUser);
        userService.addUser(newUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        System.out.println("delete " + id);
        userService.delete(userService.getUserById(id));
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping
    public void updateUser(@RequestBody User updatedUser) {
        userService.update(updatedUser);
    }
}
