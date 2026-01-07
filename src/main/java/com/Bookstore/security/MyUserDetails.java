package com.Bookstore.security;

import com.Bookstore.enums.UserStatus;
import com.Bookstore.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE:" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.BLOCKED;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
