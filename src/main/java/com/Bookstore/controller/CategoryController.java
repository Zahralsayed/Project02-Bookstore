package com.Bookstore.controller;

import com.Bookstore.dto.CreateCategoryDTO;
import com.Bookstore.dto.UpdateCategoryDTO;
import com.Bookstore.model.Category;
import com.Bookstore.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ✅ ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Category create(@Valid @RequestBody CreateCategoryDTO dto) {
        return categoryService.create(dto);
    }

    // ✅ GET ALL (ACTIVE ONLY)
    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    // ✅ ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Category update(@PathVariable Long id,
                           @Valid @RequestBody UpdateCategoryDTO dto) {
        return categoryService.update(id, dto);
    }

    // ✅ ADMIN ONLY (SOFT DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
