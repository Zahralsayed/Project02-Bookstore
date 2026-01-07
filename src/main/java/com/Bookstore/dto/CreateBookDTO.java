package com.Bookstore.dto;

import lombok.Data;

@Data
public class CreateBookDTO {
    private String name;
    private String author;
    private double price;
    private int quantity;
    private String isbn;
    private String coverImage;
    private String status;
    private Long categoryId;
}
