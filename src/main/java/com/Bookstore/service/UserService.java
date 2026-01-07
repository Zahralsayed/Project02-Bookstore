package com.Bookstore.service;

import com.Bookstore.enums.Role;
import com.Bookstore.enums.UserStatus;
import com.Bookstore.exception.InformationExistException;
import com.Bookstore.model.User;
import com.Bookstore.model.UserProfile;
import com.Bookstore.model.request.LoginRequest;
import com.Bookstore.model.response.UserResponse;
import com.Bookstore.repository.UserRepository;
import com.Bookstore.security.JWTUtils;
import com.Bookstore.security.MyUserDetails;
import com.Bookstore.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy MyUserDetailsService myUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = myUserDetailsService;
    }

    // Registration
    public ResponseEntity<?> createUser(User user) {
        System.out.println("Service Calling CreateUser ==> ");
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new InformationExistException(
                    "User with email " + user.getEmail() + " already exists"
            );
        }

        // Encode password and set default role/status
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.INACTIVE);

        UserProfile profile = new UserProfile();
        profile.setUser(user); // link profile to user
        user.setProfile(profile);

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getStatus(),
                savedUser.getProfile()
        );

        return ResponseEntity.ok(Map.of(
                "status", savedUser.getStatus(),
                "message", "User registered successfully. Please verify your email before logging in.",
                "user", response
        ));

    }


    // Login
    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        System.out.println("Service Calling loginUser ==> ");
        try {
            // Authenticate using email + password
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            // Load UserDetails by email
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
            MyUserDetails myUser = (MyUserDetails) userDetails;

            // Check if account is active
            if (myUser.getUser().getStatus() != UserStatus.ACTIVE) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Your account is inactive. Please verify your email."));
            }

            // Generate JWT
            String jwt = jwtUtils.generateToken(userDetails);

            // Return login success
            return ResponseEntity.ok(
                    Map.of(
                            "email", userDetails.getUsername(),
                            "username", myUser.getUser().getUsername(),
                            "roles", userDetails.getAuthorities(),
                            "token", jwt
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
    }

}
