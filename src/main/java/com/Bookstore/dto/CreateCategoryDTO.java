package com.Bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryDTO {
    @NotBlank
    private String name;
    private String description;
}