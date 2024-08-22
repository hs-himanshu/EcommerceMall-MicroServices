package com.example.ecommercemall.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference //response do not go in loop
    private Order order;

    @JoinColumn(name = "product_id")
    private UUID productId;

    @Transient //not included in Database
    private List<Product> product;

    @Column(nullable = false)
    private Integer quantity;
}