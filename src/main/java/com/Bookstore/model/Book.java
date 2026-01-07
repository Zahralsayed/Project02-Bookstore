package com.Bookstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false)
    private String name;

    private String author;

    private double price;

    private int quantity;

    private String isbn;

    @Column(name = "cover_image", columnDefinition = "TEXT")
    private String coverImage;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}

