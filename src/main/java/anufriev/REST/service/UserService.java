package anufriev.REST.service;

import anufriev.REST.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> allUsers();
    User getUserByName(String username);
    User getUserById(Long userId);
    void addUser(User user);
    void update(User user);
    void delete(User user);
}
