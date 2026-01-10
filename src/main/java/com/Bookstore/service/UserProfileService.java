package com.Bookstore.service;

import com.Bookstore.model.User;
import com.Bookstore.model.UserProfile;
import com.Bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UserProfileService {
    private final Path root = Paths.get("src/main/profile-pics");
    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage folder", e);
        }
    }
    public String uploadImage(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }
        String filename = user.getUsername() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();


        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        }
        UserProfile profile = user.getProfile();
        profile.setProfileImage(filename);
        userRepository.save(user);

        return filename;
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }
}
