package com.Bookstore.service;

import com.Bookstore.enums.OrderStatus;
import com.Bookstore.exception.InformationExistException;
import com.Bookstore.exception.InformationNotFoundException;
import com.Bookstore.model.Book;
import com.Bookstore.model.OrderItem;
import com.Bookstore.model.Orders;
import com.Bookstore.model.User;
import com.Bookstore.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdersService {

    private OrdersRepository ordersRepository;

    @Autowired
    public void setOrdersRepository(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public Orders createOrder(Orders order, User user) {

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new InformationExistException("Order must contain at least one item");
        }

        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total =BigDecimal.ZERO;

        for(OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
            BigDecimal subtotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setSubtotal(subtotal);
            total = total.add(subtotal);
        }

        order.setTotalPrice(total);

        return ordersRepository.save(order);
    }

    public List<Orders> findUserOrders(User user) {
        return ordersRepository.findByUserId(user.getId());
    }

    public Orders getOrderById(long id) {
        return ordersRepository.findById(id)
                .orElseThrow(()-> new InformationNotFoundException("Order with id "+ id + " not found"));
    }

}
