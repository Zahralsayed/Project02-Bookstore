package com.Bookstore.controller;

import com.Bookstore.enums.UserStatus;
import com.Bookstore.model.User;
import com.Bookstore.model.request.ChangePasswordRequest;
import com.Bookstore.model.request.ForgetPasswordRequest;
import com.Bookstore.model.request.LoginRequest;
import com.Bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("Calling registerUser ==> ");
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("Calling loginUser ==> ");
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return userService.verifyEmail(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgetPasswordRequest request) {
        String email = request.getEmail();
        String response = userService.forgotPassword(email);

        if (response.startsWith("Invalid")) {
            return ResponseEntity.badRequest().body(Map.of("message", response));
        }

        String resetUrl = "http://localhost:8080/auth/users/reset-password?token=" + response;

        return ResponseEntity.ok(Map.of(
                "message", "A reset link has been generated successfully.",
                "resetLink", resetUrl
        ));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String token,
                                                             @RequestParam String newPassword) {
        String result = userService.resetPassword(token, newPassword);

        if (result.contains("successfully")) {
            return ResponseEntity.ok(Map.of("message", result));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", result));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal){
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "You must be logged in to change your password."));
        }
        return userService.changePassword(principal.getName(), request);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll(){
        return userService.getAll();
    }

    @PatchMapping("/status/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeUserStatus(
            @PathVariable Long userId,
            @RequestParam UserStatus status) {

        userService.updateUserStatus(userId, status);
        return ResponseEntity.ok("User status successfully updated to: " + status.name());
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> softDeleteUser(@PathVariable Long userId) {
        userService.softDeleteUser(userId);
        return ResponseEntity.ok("User has been soft-deleted (Status set to INACTIVE).");
    }

}
