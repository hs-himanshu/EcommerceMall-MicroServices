package com.example.userservice.services;

import com.example.userservice.dtos.*;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse login(LogInRequest request);

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);

    String logoutUser();
}
