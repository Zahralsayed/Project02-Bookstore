package com.Bookstore.service;

import com.Bookstore.dto.CreateCategoryDTO;
import com.Bookstore.dto.UpdateCategoryDTO;
import com.Bookstore.model.Category;
import com.Bookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category create(CreateCategoryDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        return categoryRepository.save(c);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category update(Long id, UpdateCategoryDTO dto) {
        Category c = getById(id);
        if (dto.getName() != null) c.setName(dto.getName());
        if (dto.getDescription() != null) c.setDescription(dto.getDescription());
        return categoryRepository.save(c);
    }

    public void delete(Long id) {
        categoryRepository.delete(getById(id));
    }
}
