package spring.security.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*created a class to get the user login details as object in JSON*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginData {
    private String userName;
    private String password;
}
