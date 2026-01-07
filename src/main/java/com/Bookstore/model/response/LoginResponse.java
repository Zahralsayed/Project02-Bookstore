package com.Bookstore.model.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record LoginResponse(
        String email,
        Collection<? extends GrantedAuthority> roles,
        String token
) {}