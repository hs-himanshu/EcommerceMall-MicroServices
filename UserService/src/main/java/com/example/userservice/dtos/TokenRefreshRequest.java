package com.example.userservice.dtos;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}