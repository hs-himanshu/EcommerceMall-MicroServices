package com.example.ecommercemall.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userId;

    private String name;

    private String description;

    private Double price;

    private String imageUrl;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
