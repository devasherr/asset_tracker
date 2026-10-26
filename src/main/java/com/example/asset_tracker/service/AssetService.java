package com.example.asset_tracker.service;

import com.example.asset_tracker.dto.AssetRequest;
import com.example.asset_tracker.dto.AssetResponse;
import com.example.asset_tracker.entity.Asset;
import com.example.asset_tracker.entity.AssetStatus;
import com.example.asset_tracker.entity.Category;
import com.example.asset_tracker.entity.User;
import com.example.asset_tracker.exception.BusinessRuleException;
import com.example.asset_tracker.exception.ResourceNotFoundException;
import com.example.asset_tracker.repository.AssetRepository;
import com.example.asset_tracker.repository.CategoryRepository;
import com.example.asset_tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public AssetService(AssetRepository assetRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AssetResponse create(AssetRequest request) {
        if (assetRepository.existsBySerialNumber(request.serialNumber())) {
            throw new IllegalArgumentException("Asset with serial number already exists: " + request.serialNumber());
        }
        Category category = categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));

        Asset asset = new Asset();
        asset.setName(request.name());
        asset.setSerialNumber(request.serialNumber());
        asset.setCategory(category);
        asset.setStatus(AssetStatus.AVAILABLE);
        asset = assetRepository.save(asset);
        return toResponse(asset);
    }

    @Transactional
    public AssetResponse assign(Long assetId, UUID userId) {
        Asset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        if (asset.getStatus() != AssetStatus.AVAILABLE) {
            throw new BusinessRuleException("Asset is not available for assignment. Current status: " + asset.getStatus());
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        asset.setAssignedTo(user);
        asset.setStatus(AssetStatus.ASSIGNED);
        asset = assetRepository.save(asset);
        return toResponse(asset);
    }

    @Transactional
    public AssetResponse returnAsset(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));

        if (asset.getStatus() != AssetStatus.ASSIGNED) {
            throw new BusinessRuleException("Asset is not currently assigned. Current status: " + asset.getStatus());
        }

        asset.setAssignedTo(null);
        asset.setStatus(AssetStatus.AVAILABLE);
        asset = assetRepository.save(asset);
        return toResponse(asset);
    }

    @Transactional
    public void delete(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));
        assetRepository.delete(asset);
    }

    public List<AssetResponse> findAll(AssetStatus status) {
        List<Asset> assets = (status != null) ? assetRepository.findByStatus(status) : assetRepository.findAll();
        return assets.stream().map(this::toResponse).toList();
    }

    public List<AssetResponse> findMyAssets(UUID userId) {
        return assetRepository.findByAssignedTo_Id(userId).stream()
            .map(this::toResponse)
            .toList();
    }

    private AssetResponse toResponse(Asset asset) {
        return new AssetResponse(
            asset.getId(),
            asset.getName(),
            asset.getSerialNumber(),
            asset.getStatus(),
            asset.getCategory().getId(),
            asset.getCategory().getName(),
            asset.getAssignedTo() != null ? asset.getAssignedTo().getId() : null,
            asset.getAssignedTo() != null ? asset.getAssignedTo().getUsername() : null,
            asset.getCreatedAt()
        );
    }
}