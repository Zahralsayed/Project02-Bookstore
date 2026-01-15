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
        c.setStatus("ACTIVE"); // soft delete default
        return categoryRepository.save(c);
    }

    public List<Category> getAll() {
        return categoryRepository.findByStatus("ACTIVE");
    }

    //  GET BY ID (BLOCK INACTIVE)
    public Category getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!"ACTIVE".equals(category.getStatus())) {
            throw new RuntimeException("Category not found");
        }
        return category;
    }

    public Category update(Long id, UpdateCategoryDTO dto) {
        Category c = getById(id);

        if (dto.getName() != null) c.setName(dto.getName());
        if (dto.getDescription() != null) c.setDescription(dto.getDescription());

        return categoryRepository.save(c);
    }

    public void delete(Long id) {
        Category c = getById(id);
        c.setStatus("INACTIVE");
        categoryRepository.save(c);
    }
}
