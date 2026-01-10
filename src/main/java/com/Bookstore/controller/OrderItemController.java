package com.Bookstore.controller;

import com.Bookstore.model.OrderItem;
import com.Bookstore.model.User;
import com.Bookstore.service.OrderItemService;
import com.Bookstore.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private OrdersService ordersService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public OrderItem addItem(
            @RequestParam Long orderId,
            @RequestParam Long bookId,
            @RequestParam int quantity
    ) {
        System.out.println("Calling addItem ==>");
        User user = ordersService.getCurrentLoggedInUser();
        return orderItemService.addItem(orderId, bookId, quantity, user);
    }


}
