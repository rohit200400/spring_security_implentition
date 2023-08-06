package spring.security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.demo.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    void deleteByEmail(String email);
}
