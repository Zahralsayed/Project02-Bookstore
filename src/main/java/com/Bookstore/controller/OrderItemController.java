package com.Bookstore.controller;

import com.Bookstore.model.OrderItem;
import com.Bookstore.model.User;
import com.Bookstore.service.OrderItemService;
import com.Bookstore.service.OrdersService;
import com.Bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final UserService userService;

    public OrderItemController(OrderItemService orderItemService, UserService userService) {
        this.orderItemService = orderItemService;
        this.userService = userService;
    }

    @PutMapping("/{id}")
    public OrderItem updateItem(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {
        System.out.println("Calling updateItem ==>");
        User user = userService.getCurrentUser();
        return orderItemService.updateQuantity(id, quantity, user);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        System.out.println("Calling deleteItem ==>");
        User user = userService.getCurrentUser();
        orderItemService.deleteItem(id, user);
    }

}
