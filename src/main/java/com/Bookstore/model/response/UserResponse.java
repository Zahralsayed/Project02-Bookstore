package com.Bookstore.model.response;

import com.Bookstore.enums.Role;
import com.Bookstore.enums.UserStatus;
import com.Bookstore.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private UserStatus status;
    private UserProfile profile;


}
