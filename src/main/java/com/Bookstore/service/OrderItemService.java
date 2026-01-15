package com.Bookstore.service;

import com.Bookstore.enums.OrderStatus;
import com.Bookstore.model.Book;
import com.Bookstore.model.OrderItem;
import com.Bookstore.model.Orders;
import com.Bookstore.model.User;
import com.Bookstore.repository.BookRepository;
import com.Bookstore.repository.OrderItemRepository;
import com.Bookstore.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrdersRepository ordersRepository;
    private final BookRepository bookRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, OrdersRepository ordersRepository, BookRepository bookRepository) {
        this.orderItemRepository = orderItemRepository;
        this.ordersRepository = ordersRepository;
        this.bookRepository = bookRepository;
    }

    public OrderItem addItem(Long orderId, Long bookId, int quantity, User user) {
        System.out.println("Calling Service addItem ==>");
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized order access");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setBook(book);
        item.setQuantity(quantity);
        item.setUnitPrice(BigDecimal.valueOf(book.getPrice()));
        item.setSubtotal(BigDecimal.valueOf(book.getPrice()).multiply(BigDecimal.valueOf(quantity)));

    return orderItemRepository.save(item);
    }


    public OrderItem updateQuantity(Long itemId,int quantity, User user) {
        System.out.println("Calling Service updateItem ==>");
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem Not Found"));


        Orders order = item.getOrder();
        if (!order.getId().equals(user.getId())) {
        throw new RuntimeException("Unauthorized order access");
        }

        if (order.getStatus()!= OrderStatus.CREATED){
            throw new IllegalStateException("Too Late! Order is not editable in status: " + order.getStatus());
        }

        if(quantity<1){
            throw new RuntimeException("Quantity must be at least 1");
        }

    item.setQuantity(quantity);
    item.setSubtotal(
            item.getUnitPrice().multiply(BigDecimal.valueOf(quantity))
    );

    return orderItemRepository.save(item);
    }

    public void deleteItem(Long itemId, User user) {
        System.out.println("Calling Service deleteItem ==>");

        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem Not Found"));

        Orders order = item.getOrder();

        if (!order.getId().equals(user.getId())) {
        throw new RuntimeException("Unauthorized order access");
        }

        if (order.getStatus()!= OrderStatus.CREATED){
            throw new IllegalStateException("Too Late! Order is not deleted in status: " + order.getStatus());
        }

        orderItemRepository.delete(item);
    }

}
