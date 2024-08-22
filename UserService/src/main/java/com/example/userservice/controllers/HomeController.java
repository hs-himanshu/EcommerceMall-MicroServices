package com.example.userservice.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        System.out.println("Received Request");
        return ResponseEntity.ok("Helo form Home");
    }
}
