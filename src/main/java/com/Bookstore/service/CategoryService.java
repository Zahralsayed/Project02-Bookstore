package com.Bookstore.service;

import com.Bookstore.dto.CreateCategoryDTO;
import com.Bookstore.dto.UpdateCategoryDTO;
import com.Bookstore.model.Category;

import java.util.List;

public interface CategoryService {
    Category create(CreateCategoryDTO dto);
    List<Category> getAll();
    Category getById(Long id);
    Category update(Long id, UpdateCategoryDTO dto);
    void delete(Long id);
}
