package spring.security.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.security.demo.JWT.JwtAuthenticationFilter;
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
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserDetailsService userDetailsService;


    private static final String[] SECURED_URLs_ADMIN = {"users/allUsers"};

    private static final String[] SECURED_URLs_USER = {};

    private static final String[] UN_SECURED_URLs = {"users/login","users/register/save"};

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
                        .requestMatchers(SECURED_URLs_ADMIN).hasAnyAuthority("ADMIN")
                        .requestMatchers(SECURED_URLs_USER).hasAnyAuthority("USER","ADMIN")
                        .anyRequest().authenticated()
                )

                // disables form-based login
                .formLogin(
                        AbstractHttpConfigurer::disable
                )

                // added the authenticationFilter in the filter chain before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // the application will use stateless session management
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );


        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
