package anufriev.REST.repository;

import anufriev.REST.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findById(long id);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
}
