package com.Bookstore.service;

import com.Bookstore.dto.OrderItemRequestDTO;
import com.Bookstore.dto.OrderRequestDTO;
import com.Bookstore.enums.OrderStatus;
import com.Bookstore.enums.Role;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class OrdersService {

    private OrdersRepository ordersRepository;
    private BookRepository bookRepository;
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void setOrdersRepository(OrdersRepository ordersRepository, BookRepository bookRepository, UserService userService) {
        this.ordersRepository = ordersRepository;
        this.bookRepository = bookRepository;
        this.userService= userService;
    }

    @Transactional
    public Orders createOrder(OrderRequestDTO orderRequest) {
        System.out.println("Service Calling createOrder ==>");

        User user = userService.getCurrentUser();
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

            if (book.getQuantity() < itemDTO.getQuantity()) {
                throw new IllegalStateException("Not enough stock for book: " + book.getName());
            }

            book.setQuantity(book.getQuantity() - itemDTO.getQuantity());
            bookRepository.save(book);

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

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public List<Orders> findUserOrders() {
        System.out.println("Service Calling findUserOrders ==>");
        User user = userService.getCurrentUser();
        return ordersRepository.findByUserId(user.getId());
    }

    public Orders getOrderByIdAdmin(long id) {
        System.out.println("Service Calling getOrderByIdAdmin==>");
        return ordersRepository.findById(id)
                .orElseThrow(()-> new InformationNotFoundException("Order with id "+ id + " not found"));
    }

    public Orders getOrderByIdForCurrentUser(long id) {
        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new InformationNotFoundException("Order not found with id: " + id));

        if (!order.getUser().getId().equals(userService.getCurrentUser().getId())) {
            throw new RuntimeException("You cannot access this order");
        }

        return order;
    }


    public Orders updateOrderStatus(long id, OrderStatus status) {
        System.out.println("Service Calling updateOrderStatus ==>");
        Orders order = getOrderByIdAdmin(id);

        if (order.getStatus() == OrderStatus.CANCELLED){
            throw new IllegalStateException("This order was cancelled, Cancelled orders cannot be modified");
        }

        if (order.getStatus() == status) {
            throw new IllegalArgumentException("Order already has status " + status);
        }

        order.setStatus(status);
        return ordersRepository.save(order);
    }

    @Transactional
    public Orders cancelOrder(long id) {
        System.out.println("Service Calling cancelOrder ==>");
        Orders order = getOrderByIdForCurrentUser(id);

        if (order.getStatus()!= OrderStatus.CREATED){
            throw new IllegalStateException("Sorry! Too Late TO delete this order");
        }

        for (OrderItem item : order.getOrderItems()) {
            Book book = item.getBook();
            book.setQuantity(book.getQuantity() + item.getQuantity());
            bookRepository.save(book);
        }

        order.setStatus(OrderStatus.CANCELLED);

        return ordersRepository.save(order);
    }

    public void deleteCancelledOrder(long orderId) {
        Orders order = getOrderByIdAdmin(orderId);

        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new IllegalStateException("Only cancelled orders can be deleted by admin");
        }

        ordersRepository.delete(order);
        System.out.println("Admin deleted cancelled order with ID: " + orderId);
    }

}
