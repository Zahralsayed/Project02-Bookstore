package com.Bookstore.service;

import com.Bookstore.dto.CreateBookDTO;
import com.Bookstore.dto.UpdateBookDTO;
import com.Bookstore.model.Book;
import com.Bookstore.model.Category;
import com.Bookstore.repository.BookRepository;
import com.Bookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Book create(CreateBookDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        Book b = new Book();
        b.setName(dto.getName());
        b.setAuthor(dto.getAuthor());
        b.setPrice(dto.getPrice());
        b.setQuantity(dto.getQuantity());
        b.setIsbn(dto.getIsbn());
        b.setCoverImage(dto.getCoverImage());
        b.setStatus(dto.getStatus());
        b.setCategory(category);
        b.setCreatedAt(LocalDateTime.now());
        b.setUpdatedAt(LocalDateTime.now());

        return bookRepository.save(b);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Override
    public Book update(Long id, UpdateBookDTO dto) {
        Book b = getById(id);

        if (dto.getName() != null) b.setName(dto.getName());
        if (dto.getAuthor() != null) b.setAuthor(dto.getAuthor());
        if (dto.getPrice() != null) b.setPrice(dto.getPrice());
        if (dto.getQuantity() != null) b.setQuantity(dto.getQuantity());
        if (dto.getIsbn() != null) b.setIsbn(dto.getIsbn());
        if (dto.getCoverImage() != null) b.setCoverImage(dto.getCoverImage());
        if (dto.getStatus() != null) b.setStatus(dto.getStatus());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            b.setCategory(category);
        }

        b.setUpdatedAt(LocalDateTime.now());
        return bookRepository.save(b);
    }

    @Override
    public void delete(Long id) {
        Book b = getById(id);
        bookRepository.delete(b);
    }
}
