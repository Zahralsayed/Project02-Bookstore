package com.Bookstore.service;

import com.Bookstore.model.User;
import com.Bookstore.model.UserProfile;
import com.Bookstore.model.request.UserProfileUpdateRequest;
import com.Bookstore.repository.UserProfileRepository;
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
import java.util.List;

@Service
public class UserProfileService {
    private final Path root = Paths.get("src/main/profile-pics");
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
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

    public UserProfile updateFullProfile(String email, UserProfileUpdateRequest request) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserProfile profile = user.getProfile();

        if (request != null) {
            if (request.phone() != null) profile.setPhone(request.phone());
            if (request.address() != null) profile.setAddress(request.address());
            if (request.dateOfBirth() != null) profile.setDateOfBirth(request.dateOfBirth());

            MultipartFile file = request.file();
            if (file != null && !file.isEmpty()) {
                if (profile.getProfileImage() != null) {
                    Path oldPath = root.resolve(profile.getProfileImage());
                    Files.deleteIfExists(oldPath);
                }

                String newFilename = user.getUsername() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), this.root.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);

                profile.setProfileImage(newFilename);
            }
        }

        userRepository.save(user);
        return profile;
    }
}
