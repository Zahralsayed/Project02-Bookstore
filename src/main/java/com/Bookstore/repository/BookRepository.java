package com.Bookstore.repository;



import com.Bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByStatus(String status);
    List<Book> findByCategoryCategoryIdAndStatus(Long categoryId, String status);

}
