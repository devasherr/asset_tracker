package com.example.asset_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AssetRequest(
    @NotBlank(message = "Asset name is required")
    @Size(min = 1, max = 255, message = "Asset name must be between 1 and 255 characters")
    String name,

    @NotBlank(message = "Serial number is required")
    @Size(min = 1, max = 255, message = "Serial number must be between 1 and 255 characters")
    String serialNumber,

    @NotNull(message = "Category ID is required")
    Long categoryId
) {}