package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface used for shipment status database operations.
 * Provides methods for managing shipment status history.
 */
public interface ShipmentStatusRepository extends JpaRepository<ShipmentStatus, Integer> {

    /**
     * Returns all statuses for a specific shipment,
     * ordered by creation date in descending order.
     *
     * @param shipmentId the shipment identifier
     * @return list of shipment statuses
     */
    List<ShipmentStatus> findByShipment_IdOrderByCreatedOnDesc(Integer shipmentId);

    /**
     * Deletes all statuses associated with a specific shipment.
     *
     * @param shipmentId the shipment identifier
     */
    void deleteByShipment_Id(Integer shipmentId);
}