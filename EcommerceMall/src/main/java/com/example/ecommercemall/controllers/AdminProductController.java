package com.example.ecommercemall.controllers;

import com.example.ecommercemall.dtos.ReceivedOrderResponseDTO;
import com.example.ecommercemall.dtos.ResponseDTO;
import com.example.ecommercemall.models.Product;
import com.example.ecommercemall.services.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        //it gets the list of all servers of userservice running from euraka server
//        restTemplate.getForObject("http://UserService/home/hello", String.class);
        return "Hello World from Admin Service";
    }

    @PostMapping("/product")
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody Product product) {
        adminProductService.createProduct(product);
        ResponseDTO response = new ResponseDTO("Product created successfully!", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-products/{userId}")
    public ResponseEntity<List<Product>> getMyProducts(@PathVariable String userId){
        List<Product> response = adminProductService.getAllProducts(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<ResponseDTO> deleteProduct(@PathVariable UUID id){
        adminProductService.deleteProduct(id);
        ResponseDTO response = new ResponseDTO("Product deleted successfully!", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/received-orders/{userId}")
    public ResponseEntity<List<ReceivedOrderResponseDTO>> getAllReceivedOrders(@PathVariable String userId){
        List<ReceivedOrderResponseDTO> receivedOrders = adminProductService.getAllReceivedOrders(userId);
        return ResponseEntity.ok(receivedOrders);
    }
}
