package com.Bookstore.service;

import com.Bookstore.dto.CreateBookDTO;
import com.Bookstore.dto.UpdateBookDTO;
import com.Bookstore.model.Book;

import java.util.List;

public interface BookService {
    Book create(CreateBookDTO dto);
    // Public -  Available only
    List<Book> getAll();

    Book getById(Long id);

    // Admin - all books
    Book getByIdForAdmin(Long id);
    List<Book> getAllForAdmin();

    Book update(Long id, UpdateBookDTO dto);
    void delete(Long id);
    List<Book> getBooksByCategory(Long categoryId);
}
