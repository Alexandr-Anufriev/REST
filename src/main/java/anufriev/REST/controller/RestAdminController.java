package anufriev.REST.controller;

import anufriev.REST.model.Provider;
import anufriev.REST.model.User;
import anufriev.REST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/admin")
public class RestAdminController {
    private final Logger LOGGER = Logger.getLogger("<------ LOG: RestAdminController ------>");
    private final UserService userService;

    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> showAllUsers() {
        LOGGER.info("showAllUsers");
        return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        LOGGER.info("getUser " + userService.getUserById(id));
        return  new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User newUser){
        newUser.setProvider(Provider.LOCAL);
        userService.addUser(newUser);
        LOGGER.info("addUser " + newUser.getUsername());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.delete(userService.getUserById(id));
        LOGGER.info("deleteUser with id: " + id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        LOGGER.info("updateUser with id: " + updatedUser.getId());
        userService.update(updatedUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
