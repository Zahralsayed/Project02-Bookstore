package com.Bookstore.service;

import com.Bookstore.dto.OrderItemRequestDTO;
import com.Bookstore.dto.OrderRequestDTO;
import com.Bookstore.enums.OrderStatus;
import com.Bookstore.exception.InformationExistException;
import com.Bookstore.exception.InformationNotFoundException;
import com.Bookstore.model.Book;
import com.Bookstore.model.OrderItem;
import com.Bookstore.model.Orders;
import com.Bookstore.model.User;
import com.Bookstore.repository.BookRepository;
import com.Bookstore.repository.OrdersRepository;
import com.Bookstore.repository.UserRepository;
import com.Bookstore.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdersService {

    private OrdersRepository ordersRepository;
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void setOrdersRepository(OrdersRepository ordersRepository, BookRepository bookRepository) {
        this.ordersRepository = ordersRepository;
        this.bookRepository = bookRepository;
    }

    public Orders createOrder(OrderRequestDTO orderRequest, User user) {
        System.out.println("Service Calling createOrder ==>");

        if (orderRequest.getOrderItems() == null || orderRequest.getOrderItems().isEmpty()) {
            throw new InformationExistException("Order must contain at least one item");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : orderRequest.getOrderItems()) {
            if (itemDTO.getBookId() == null) {
                throw new InformationExistException("Book is required for each order item");
            }
            if (itemDTO.getQuantity() == null || itemDTO.getQuantity() <= 0) {
                throw new InformationExistException("Quantity must be greater than 0 for bookId: " + itemDTO.getBookId());
            }

            Book book = bookRepository.findById(itemDTO.getBookId())
                    .orElseThrow(() -> new InformationExistException("Book not found with id: " + itemDTO.getBookId()));

            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(BigDecimal.valueOf(book.getPrice()));
            item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setOrder(order);

            order.getOrderItems().add(item);
            total = total.add(item.getSubtotal());
        }

        order.setTotalPrice(total);

        return ordersRepository.save(order);
    }

    public List<Orders> findUserOrders(User user) {
        System.out.println("Service Calling findUserOrders ==>");
        return ordersRepository.findByUserId(user.getId());
    }

    public Orders getOrderById(long id) {
        System.out.println("Service Calling getOrderById==>");
        return ordersRepository.findById(id)
                .orElseThrow(()-> new InformationNotFoundException("Order with id "+ id + " not found"));
    }

    public Orders updateOrderStatus(long id, OrderStatus status) {
        System.out.println("Service Calling updateOrderStatus ==>");
        Orders order = getOrderById(id);
        if (order.getStatus() == OrderStatus.CANCELED){
            throw new IllegalStateException("This order was canceled, Canceled orders cannot be modified");
        }

        if (order.getStatus() == status) {
            throw new IllegalArgumentException("Order already has status " + status);
        }

        order.setStatus(status);
        return ordersRepository.save(order);
    }

    public Orders cancelOrder(long id) {
        System.out.println("Service Calling cancelOrder ==>");
        Orders order = getOrderById(id);

        if (order.getStatus()!= OrderStatus.CREATED){
            throw new IllegalStateException("Sorry! Too Late TO delete this order");
        }

        for (OrderItem item : order.getOrderItems()) {
            Book book = item.getBook();
            book.setQuantity(book.getQuantity() + item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELED);

        return ordersRepository.save(order);
    }

//    public static User getCurrentLoggedInUser(){
//        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return userDetails.getUser();
//    }

    public User getCurrentLoggedInUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("No user is currently logged in");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof MyUserDetails myUserDetails) {
            return myUserDetails.getUser();
        } else if (principal instanceof String username) {
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
        } else {
            throw new RuntimeException("Unexpected principal type: " + principal.getClass().getName());

        }

    }
}
