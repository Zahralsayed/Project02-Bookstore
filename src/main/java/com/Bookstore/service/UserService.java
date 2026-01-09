package com.Bookstore.service;

import com.Bookstore.enums.Role;
import com.Bookstore.enums.UserStatus;
import com.Bookstore.exception.InformationExistException;
import com.Bookstore.model.User;
import com.Bookstore.model.UserProfile;
import com.Bookstore.model.request.LoginRequest;
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

import java.time.LocalDateTime;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager,
                       @Lazy MyUserDetailsService myUserDetailsService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = myUserDetailsService;
        this.tokenService = tokenService;
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

        String token = tokenService.generateToken();
        user.setVerificationToken(token);
        user.setVerificationTokenDate(LocalDateTime.now());
        userRepository.save(user);

        sendVerificationEmail(user, token);

        return ResponseEntity.ok(Map.of(
                "status", user.getStatus(),
                "message", "User registered successfully. Please verify your email before logging in.",
                "verificationUrl", "http://localhost:8080/auth/users/verify?token=" + token
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
                User user = myUser.getUser();
                String token = tokenService.generateToken();

                user.setVerificationToken(token);
                user.setVerificationTokenDate(LocalDateTime.now());
                userRepository.save(user);

                sendVerificationEmail(myUser.getUser(), token);

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "error", "Your account is inactive. Please verify your email.",
                                "verificationUrl", "http://localhost:8080/auth/users/verify?token=" + token
                        ));
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

    public ResponseEntity<?> verifyEmail(String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token is missing."));
        }
        try {
            User user = userRepository.findByVerificationToken(token)
                    .orElseThrow(() -> new RuntimeException("This link is invalid or has already been used."));

            if (tokenService.isTokenExpired(user.getVerificationTokenDate())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Link expired. Please login to receive a new one."));
            }
            if (user.getStatus() == UserStatus.ACTIVE) {
                return ResponseEntity.ok(Map.of("message", "User already verified."));
            }

            user.setStatus(UserStatus.ACTIVE);
            user.setVerificationToken(null);
            user.setVerificationTokenDate(null);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("message", "Email verified successfully. You can now log in."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid or expired verification token."));
        }
    }
    private void sendVerificationEmail(User user, String token) {
        String verifyUrl = "http://localhost:8080/auth/users/verify?token=" + token;

        String body = String.format(
                "Hello %s,\n\n" +
                        "Thank you for registering! To complete your sign-up, please click the link below to verify your email address:\n\n" +
                        "%s\n\n" +
                        "Note: This link is valid for 15 minutes.\n\n" +
                        "If you did not create an account, please ignore this email.",
                user.getUsername(), verifyUrl
        );

        logger.info("Email sent to {}: \n{}", user.getEmail(), body);
    }

}
