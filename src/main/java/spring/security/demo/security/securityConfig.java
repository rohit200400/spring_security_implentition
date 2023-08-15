package spring.security.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import spring.security.demo.repository.UserRepo;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class securityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserDetailsService userDetailsService;


    private static final String[] SECURED_URLs = {"users/allUsers", "users/homepage"};

    private static final String[] UN_SECURED_URLs = {"users/register/save"};

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*This configuration is used in a Spring Boot application to define how HTTP requests
    should be authorized and secured.
    this configuration sets up the security filter chain to disable CSRF protection, define authorization rules for
    different URL patterns, disable form-based login, and configure HTTP Basic authentication for secured URLs.*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // disabling the Cross-site request forgery because APIs are not meant to be accessed via browsers
                .csrf(AbstractHttpConfigurer::disable)

                // authorization define access rules for different URL patterns.
                .authorizeHttpRequests((authorize) ->authorize
                        .requestMatchers(UN_SECURED_URLs).permitAll()
                        .requestMatchers(SECURED_URLs).authenticated()
                        //.hasAnyAuthority("ADMIN", "USER")
                )
                .oauth2Login(withDefaults())
                // disables form-based login
                .formLogin(
                        withDefaults()
                );
                // configures HTTP Basic authentication
                // the application expects the client to provide the username and password in the request headers
                //.httpBasic(withDefaults());
        return http.build();
    }

    /*This is the method signature for configuring global authentication. The method is public, returns void (nothing),
    and takes an AuthenticationManagerBuilder object as a parameter. The AuthenticationManagerBuilder is a class
    provided by Spring Security to help with configuring authentication.

    .userDetailsService(userDetailsService): This line sets the UserDetailsService implementation to be used by Spring
    Security for loading user details during authentication. The UserDetailsService is responsible for retrieving user
    information (e.g., username, password, and authorities) from a data source like a database.

    .passwordEncoder(passwordEncoder()): This line sets the password encoder to be used by Spring Security for hashing
    and verifying user passwords. The passwordEncoder() method should return a PasswordEncoder instance, which is
    responsible for encoding and decoding passwords.

    The actual implementation of the UserDetailsService and PasswordEncoder must be provided elsewhere in the codebase.*/
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // userDetailsService is implemented by CustomUserDetailsService class
                .userDetailsService(userDetailsService)
                // passwordEncoder is created in this class itself
                .passwordEncoder(passwordEncoder());
    }
}
