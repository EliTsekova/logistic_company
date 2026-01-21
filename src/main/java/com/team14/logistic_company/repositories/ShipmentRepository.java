package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    List<Shipment> findAllByOrderByCreatedOnDesc();

    List<Shipment> findAllByEmployeeId(Integer employeeId);

    Shipment findByUniqueId(String uniqueId);

    @Query("SELECT s FROM Shipment s WHERE s.id NOT IN (SELECT ss.shipment.id FROM ShipmentStatus ss WHERE ss.status = :deliveredStatus)")
    List<Shipment> findUndeliveredShipments(@Param("deliveredStatus") Status deliveredStatus);

}