package com.Bookstore.dto;

import lombok.Data;

@Data
public class UpdateBookDTO {
    private String name;
    private String author;
    private Double price;
    private Integer quantity;
    private String isbn;
    private String coverImage;
    private String status;
    private Long categoryId;
}
