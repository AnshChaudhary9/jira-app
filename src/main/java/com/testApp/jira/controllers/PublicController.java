package com.testApp.jira.controllers;

import com.testApp.jira.entities.User;
import com.testApp.jira.services.JWTService;
import com.testApp.jira.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        User user = userService.findByFullName(loginRequest.getFullName());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(
                            user.getFullName(),
                            user.getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );
            Map<String, Object> extraClaims = Map.of("role", user.getRole().name());
            String jwt = jwtService.generateToken(extraClaims,userDetails);


            return ResponseEntity.ok(jwt);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
