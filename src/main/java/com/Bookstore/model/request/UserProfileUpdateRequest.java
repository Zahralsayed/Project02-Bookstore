package com.Bookstore.model.request;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record UserProfileUpdateRequest (
        String phone,
        String address,
        LocalDate dateOfBirth,
        MultipartFile file
){}
