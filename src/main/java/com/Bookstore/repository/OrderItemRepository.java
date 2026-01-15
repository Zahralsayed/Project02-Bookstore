package com.Bookstore.repository;

import com.Bookstore.model.OrderItem;
import com.Bookstore.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Orders order);
    Optional<OrderItem> findByIdAndOrder(Long id, Orders order);
}
