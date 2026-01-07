package com.Bookstore.repository;

import com.Bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category, Long>  {


}
