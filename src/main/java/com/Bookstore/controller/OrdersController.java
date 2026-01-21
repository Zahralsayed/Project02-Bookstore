package com.Bookstore.controller;

import com.Bookstore.dto.OrderRequestDTO;
import com.Bookstore.enums.OrderStatus;
import com.Bookstore.model.Orders;
import com.Bookstore.model.User;
import com.Bookstore.service.OrdersService;
import com.Bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/new")
    public Orders createOrder(@RequestBody OrderRequestDTO orderRequest) {
        System.out.println("Calling createOrder ==>");
        return ordersService.createOrder(orderRequest);
    }

    @GetMapping
    public List<Orders> getMyOrders() {
        System.out.println("Calling getMyOrders ==>");
        return ordersService.findUserOrders();
    }

    @GetMapping("/{id}")
    public Orders getOrderById(@PathVariable long id) {
        System.out.println("Calling getOrderById ==>");
        return ordersService.getOrderByIdForCurrentUser(id);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders getOrderByIdAdmin(@PathVariable long id) {
        System.out.println("Calling getOrderByIdAdmin ==>");
        return ordersService.getOrderByIdAdmin(id);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Orders> getAllOrders() {
        System.out.println("Calling getAllOrders ==>");
        return ordersService.getAllOrders();
    }

    @PutMapping("/{id}/cancel")
    public Orders cancelOrder(@PathVariable long id) {
        System.out.println("Calling cancelOrder ==>");
        return ordersService.cancelOrder(id);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Orders updateOrderStatus(
            @PathVariable long id,
            @RequestParam OrderStatus status
    ){
        System.out.println("Calling updateOrderStatus ==>");
        return ordersService.updateOrderStatus(id, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        ordersService.deleteCancelledOrder(id);
    }



}

