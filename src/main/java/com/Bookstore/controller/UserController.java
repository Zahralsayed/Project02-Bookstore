package com.Bookstore.controller;

import com.Bookstore.model.User;
import com.Bookstore.model.request.LoginRequest;
import com.Bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
