package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Integer> {
    List<ShipmentStatus> findByShipment_IdOrderByCreatedOnDesc(Integer shipmentId);
    void deleteByShipment_Id(Integer shipmentId);
}
