package com.Bookstore.dto;

import com.Bookstore.model.Orders;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Data
public class OrderItemRequestDTO {

    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    private Integer quantity;

}
