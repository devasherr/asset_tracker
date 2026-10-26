package com.example.asset_tracker.controller;

import com.example.asset_tracker.dto.AssetRequest;
import com.example.asset_tracker.dto.AssetResponse;
import com.example.asset_tracker.entity.AssetStatus;
import com.example.asset_tracker.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public List<AssetResponse> list(@RequestParam(required = false) AssetStatus status) {
        return assetService.findAll(status);
    }

    @GetMapping("/my-assets")
    public List<AssetResponse> myAssets() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        // For demo, we need to resolve username to UUID via UserService. 
        // We'll keep simple: use username as identifier for now.
        // In real app, inject UserService and map username -> UUID.
        // Placeholder: return empty for now – will be wired via service.
        return List.of(); // TODO: wire with UserService
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponse> create(@Valid @RequestBody AssetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assetService.create(request));
    }

    @PatchMapping("/{id}/assign/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponse> assign(@PathVariable Long id, @PathVariable UUID userId) {
        return ResponseEntity.ok(assetService.assign(id, userId));
    }

    @PatchMapping("/{id}/return")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponse> returnAsset(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.returnAsset(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}