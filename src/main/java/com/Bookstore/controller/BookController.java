package com.Bookstore.controller;

import com.Bookstore.dto.BookAutofillDto;
import com.Bookstore.dto.CreateBookDTO;
import com.Bookstore.dto.UpdateBookDTO;
import com.Bookstore.model.Book;
import com.Bookstore.service.BookService;
import com.Bookstore.service.ExternalBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ExternalBookService externalBookService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/autofill/{isbn}")
    public ResponseEntity<BookAutofillDto> getAutofillData(@PathVariable String isbn) {
        return ResponseEntity.ok(externalBookService.fetchBookByIsbn(isbn));
    }

    @PostMapping
    public Book create(@Valid @RequestBody CreateBookDTO dto) {
        return bookService.create(dto);
    }

//  the available only
    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Book update(@PathVariable Long id,
                       @Valid @RequestBody UpdateBookDTO dto) {
        return bookService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Book> getBooksByCategory(@PathVariable Long categoryId) {
        return bookService.getBooksByCategory(categoryId);
    }
}
