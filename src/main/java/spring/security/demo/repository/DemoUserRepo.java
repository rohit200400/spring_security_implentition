package spring.security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.demo.entity.DemoUser;

import java.util.Optional;

public interface DemoUserRepo extends JpaRepository<DemoUser, Integer> {
    Optional<DemoUser> findByEmail(String email);

    void deleteByEmail(String email);
}
