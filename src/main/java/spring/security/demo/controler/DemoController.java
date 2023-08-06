package spring.security.demo.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.security.demo.JWT.JwtTokenProvider;
import spring.security.demo.entity.LoginData;
import spring.security.demo.entity.User;
import spring.security.demo.entity.UserDto;
import spring.security.demo.service.CustomUserDetailsService;
import spring.security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class DemoController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        // fetching all the users from the database
        List<UserDto> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody LoginData loginData) throws Exception{
        // creating an authentication object from the login data supplied from the User
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginData.getUserName(),loginData.getPassword()));

        // if authentication is successful we will generate and return JWT
        if(authentication.isAuthenticated()){
            String token = jwtTokenProvider.generateToken(authentication);
            return new ResponseEntity(token,HttpStatus.OK);
        }

        // if not authenticated then we will throw exception
        else {
            throw new Exception("Bad Credentials");
        }
    }

    @PostMapping("/register/save")
    public String registration(@RequestBody UserDto userDto) throws Exception{
        // checking if any user exits with same username
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        // if exist then throw exception
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            throw new Exception("There is already an account registered with the same email");
        }

        // else create a user
        userService.saveUser(userDto);
        return "Successfully registered User.";
    }

}
