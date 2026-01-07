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


}
