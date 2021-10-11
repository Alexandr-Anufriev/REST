package anufriev.REST.config;

import anufriev.REST.model.Role;
import anufriev.REST.model.User;
import anufriev.REST.repository.RoleRepo;
import anufriev.REST.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class PostConstructConfig {

    UserRepo userRepo;
    RoleRepo roleRepo;

    @Autowired
    public PostConstructConfig(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    public void addRolesAndUsersInDB() {
        checkRole("ROLE_ADMIN");
        checkRole("ROLE_USER");
        checkAdmin();
        checkUser();
    }

    private void checkRole(String roleName) {
        if(!roleRepo.existsByName(roleName)){
            roleRepo.save(new Role(roleName));
        }
    }

    private void checkAdmin() {
        if (!userRepo.existsByUsername("admin")) {
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepo.findByName("ROLE_ADMIN"));
            roleSet.add(roleRepo.findByName("ROLE_USER"));
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder().encode("admin"));
            user.setLastname("adminovyan");
            user.setEmail("admin@mail.ru");
            user.setAge(28);
            user.setRoles(roleSet);
            userRepo.save(user);
        }
    }

    private void checkUser() {
        if(!userRepo.existsByUsername("user")){
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleRepo.findByName("ROLE_USER"));
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder().encode("user"));
            user.setLastname("userzian");
            user.setEmail("user@mail.ru");
            user.setAge(37);
            user.setRoles(roleSet);
            userRepo.save(user);
        }
    }
}
