package com.example.asset_tracker.repository;

import com.example.asset_tracker.entity.Asset;
import com.example.asset_tracker.entity.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByStatus(AssetStatus status);
    List<Asset> findByAssignedTo_Id(UUID userId);
    boolean existsBySerialNumber(String serialNumber);
}