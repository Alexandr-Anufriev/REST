package anufriev.REST.service;

import anufriev.REST.model.Provider;
import anufriev.REST.model.Role;
import anufriev.REST.model.User;
import anufriev.REST.repository.RoleRepo;
import anufriev.REST.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void addUser(User user) {
        String pass = passwordEncoder.encode(user.getPassword());
        user.setPassword(pass);
        userRepo.save(user);
    }

    @Override
    public void update(User user) {
        // проверка наличия пароля
        if (user.getPassword()=="") {
            String password = userRepo.getById(user.getId()).getPassword();
            user.setPassword(password);
        } else {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
        }
        // проверка наличия ролей
        if (user.getRoles().isEmpty()){
            user.setRoles(userRepo.getById(user.getId()).getRoles());
        }
        user.setProvider(userRepo.getById(user.getId()).getProvider());
        userRepo.save(user);
    }

    @Override
    public void delete(User user) {
        userRepo.delete(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public List<User> allUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByName(String name) {
        return userRepo.findByUsername(name);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username);
    }
}
