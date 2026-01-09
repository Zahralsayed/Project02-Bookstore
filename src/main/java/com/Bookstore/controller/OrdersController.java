package com.Bookstore.controller;

import com.Bookstore.enums.OrderStatus;
import com.Bookstore.model.Orders;
import com.Bookstore.model.User;
import com.Bookstore.service.OrdersService;
import com.Bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private OrdersService ordersService;

    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/new")
    public Orders createOrder(@RequestBody Orders order) {
        User currentUser = ordersService.getCurrentLoggedInUser();
        return ordersService.createOrder(order, currentUser);
    }

    @GetMapping
    public List<Orders> getMyOrders() {
        User currentUser = ordersService.getCurrentLoggedInUser();
        return ordersService.findUserOrders(currentUser);
    }

    @GetMapping("/{id}")
    public Orders getOrderById(@PathVariable long id) {
        return ordersService.getOrderById(id);
    }

    @PutMapping("/{id}/cancel")
    public Orders cancelOrder(@PathVariable long id) {
        return ordersService.cancelOrder(id);
    }

    @PutMapping("/{id}/status")
    public Orders updateOrderStatus(
            @PathVariable long id,
            @RequestParam OrderStatus status
    ) {
        return ordersService.updateOrderStatus(id, status);
    }




}

