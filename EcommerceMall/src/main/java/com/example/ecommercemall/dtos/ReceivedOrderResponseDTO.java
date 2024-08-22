package com.example.ecommercemall.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedOrderResponseDTO {
    String orderId;
    String productId;
    String productName;
    String productDescription;
    Integer quantity;
    Double price;
    String imgUrl;
}
