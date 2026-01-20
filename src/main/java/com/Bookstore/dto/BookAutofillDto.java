package com.Bookstore.dto;
public record BookAutofillDto(
        String name,
        String author,
        String isbn,
        String coverImage,
        String description
) {}