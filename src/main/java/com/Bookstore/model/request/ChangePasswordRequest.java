package com.Bookstore.model.request;

public record ChangePasswordRequest (
    String oldPassword,
    String newPassword,
    String confirmNewPassword
){}
