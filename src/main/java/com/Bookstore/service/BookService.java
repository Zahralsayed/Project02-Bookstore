package com.Bookstore.service;

import com.Bookstore.dto.CreateBookDTO;
import com.Bookstore.dto.UpdateBookDTO;
import com.Bookstore.model.Book;

import java.util.List;

public interface BookService {
    Book create(CreateBookDTO dto);
    List<Book> getAll();
    Book getById(Long id);
    Book update(Long id, UpdateBookDTO dto);
    void delete(Long id);
    List<Book> getBooksByCategory(Long categoryId);
}
