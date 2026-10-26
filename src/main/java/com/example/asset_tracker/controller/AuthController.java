package com.example.asset_tracker.controller;

import com.example.asset_tracker.dto.RegisterRequest;
import com.example.asset_tracker.dto.UserResponse;
import com.example.asset_tracker.entity.Role;
import com.example.asset_tracker.security.JwtUtil;
import com.example.asset_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        String token = jwtUtil.generateToken(user.username(), user.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "token", token,
            "user", user
        ));
    }
}