package com.example.ecommercemall.dtos;

import com.example.ecommercemall.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private UUID orderId;
    private Instant orderDate;
    private Double total;
    private OrderStatus orderStatus;
    private List<OrderItemResponseDTO> orderItems;
}