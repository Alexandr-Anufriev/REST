package anufriev.REST.controller;

import anufriev.REST.model.User;
import anufriev.REST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> showAllUsers() {
        return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
    }

//    @GetMapping("/currentUser")
//    public ResponseEntity<User> currentUser(Principal principal) {
//        return new ResponseEntity<>(userService.getUserByName(principal.getName()), HttpStatus.OK) ;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return  new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User newUser){
        userService.addUser(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.delete(userService.getUserById(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        userService.update(updatedUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
