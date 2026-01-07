package com.Bookstore.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateBookDTO {

    @NotBlank
    private String name;

    private String author;

    @Min(0)
    private double price;

    @Min(0)
    private int quantity;

    private String isbn;
    private String coverImage;
    private String status;

    @NotNull
    private Long categoryId;
}
