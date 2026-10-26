package com.example.asset_tracker.dto;

import com.example.asset_tracker.entity.Role;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    Role role
) {}