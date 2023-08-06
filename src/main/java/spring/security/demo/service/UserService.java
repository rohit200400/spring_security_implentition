package spring.security.demo.service;



import spring.security.demo.entity.User;
import spring.security.demo.entity.UserDto;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}