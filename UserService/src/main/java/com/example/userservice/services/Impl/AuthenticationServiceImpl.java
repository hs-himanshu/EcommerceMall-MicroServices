package com.example.userservice.services.Impl;

import com.example.userservice.configs.JwtService;
import com.example.userservice.dtos.*;
import com.example.userservice.models.RefreshToken;
import com.example.userservice.models.Role;
import com.example.userservice.models.User;
import com.example.userservice.repositories.RefreshTokenRepository;
import com.example.userservice.repositories.UserRepository;
import com.example.userservice.services.AuthenticationService;
import com.example.userservice.services.CustomUserDetails;
import com.example.userservice.services.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {

        // Check if the user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("This email address is already associated with an account. Please sign in or use a different email.");
        }

        User user = new User();
        user.setUsername(request.getFirstName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        Role role = new Role();
        role.setName("USER");
        user.setRoles(Collections.singletonList(role));

        //send message to kafka for sending user confirmation Email
        SendEmailMessageDTO messageDTO = new SendEmailMessageDTO();
        messageDTO.setTo(request.getEmail());
        messageDTO.setSubject("Welcome to EcommerceMall - You're All Set!");
        messageDTO.setBody("Thanks for signing up with EcommerceMall. Your account is now active.");
        try {
            kafkaTemplate.send("sendEmail",objectMapper.writeValueAsString(messageDTO));
        }
        catch (Exception e){
            e.printStackTrace();
        }


        userRepository.save(user);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getUsername());

        // Create a map for extra claims
        Map<String, Object> extraClaims = new HashMap<>();

        // Add roles to the extraClaims
        extraClaims.put("roles", userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList()));
        extraClaims.put("user_id",userDetails.getId());
        extraClaims.put("email",userDetails.getEmail());
        String jwt = jwtService.generateToken(extraClaims,userDetails);

        // Create and associate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return JwtAuthenticationResponse.builder().accessToken(jwt)
                .refreshToken(refreshToken.getToken()).build();
    }

    @Override
    public JwtAuthenticationResponse login(LogInRequest request) {
        try {
            // Create the authentication token
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            // Authenticate the user using the AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // Set the authentication in the context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Fetch the user details from the repository
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Incorrect username or password. Please try again."));

            // Generate the JWT token
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getUsername());

            // Create a map for extra claims
            Map<String, Object> extraClaims = new HashMap<>();

            // Add roles to the extraClaims
            //Here I am adding roles in extra claims so other micro service can provide
            //role based access to the user (ADMIN,USER)
            extraClaims.put("roles", userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.toList()));
            extraClaims.put("user_id",userDetails.getId());
            extraClaims.put("email",userDetails.getEmail());
            String jwt = jwtService.generateToken(extraClaims,userDetails);
            //Check if token already exist as saved when sign up
            Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userDetails.getId());
            RefreshToken refreshToken ;

            if(existingToken.isPresent()) {
                 refreshToken = existingToken.get();
            }
            else {

                refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
            }
            // Return the response with the token
            return JwtAuthenticationResponse.builder().accessToken(jwt)
                    .refreshToken(refreshToken.getToken()).build();
        } catch (BadCredentialsException e) {
            return JwtAuthenticationResponse.builder().accessToken("Incorrect username or password. Please try again.").build();
        }
    }


    @Transactional
    @Override
    public String logoutUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenService.deleteByUserId(userDetails.getId());
        return "Log out successful!";
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
                            .loadUserByUsername(user.getUsername());
                    // Create a map for extra claims
                    //Here I am adding roles in extra claims so other micro service can provide
                    //role based access to the user (ADMIN,USER)
                    Map<String, Object> extraClaims = new HashMap<>();

                    // Add roles to the extraClaims
                    extraClaims.put("roles", userDetails.getAuthorities().stream()
                            .map(grantedAuthority -> grantedAuthority.getAuthority())
                            .collect(Collectors.toList()));
                    extraClaims.put("user_id",userDetails.getId());
                    extraClaims.put("email",userDetails.getEmail());
                    String token = jwtService.generateToken(extraClaims,userDetails);
                    return new TokenRefreshResponse(token, requestToken); //new jwt token with same refresh token
                })
                .orElseThrow(() -> new RuntimeException("The provided refresh token is not valid or has expired. Please log in again!"));
    }

}
