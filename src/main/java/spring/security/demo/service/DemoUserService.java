package spring.security.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.security.demo.entity.DemoUser;
import spring.security.demo.repository.DemoUserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DemoUserService {
    private DemoUserRepo demoUserRepo;
    private PasswordEncoder passwordEncoder;

    public DemoUser add(DemoUser user) {
        Optional<DemoUser> theUser = demoUserRepo.findByEmail(user.getEmail());
        if (theUser.isPresent()){
            return null;
        }
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return demoUserRepo.save(user);
        }
    }

    public List<DemoUser> getAllUsers() {
        return demoUserRepo.findAll();
    }

    @Transactional
    public void deletebyEmail(String email) {
        demoUserRepo.deleteByEmail(email);
    }

    public Optional getUser(String email) {
        return demoUserRepo.findByEmail(email);
    }

    public DemoUser update(DemoUser user) {
        user.setRole(user.getRole());
        return demoUserRepo.save(user);
    }

}
