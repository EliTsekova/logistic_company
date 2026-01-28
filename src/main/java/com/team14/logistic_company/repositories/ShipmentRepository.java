package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    List<Shipment> findAllByOrderByCreatedOnDesc();

    List<Shipment> findAllByEmployeeId(Integer employeeId);

    Optional<Shipment> findByUniqueId(String uniqueId);

    // Клиент: изпратени / получени
    List<Shipment> findAllBySender_User_Id(Integer userId);
    List<Shipment> findAllByRecipient_User_Id(Integer userId);

    @Query("""
        SELECT s FROM Shipment s
        WHERE s.id NOT IN (
            SELECT ss.shipment.id FROM ShipmentStatus ss
            WHERE ss.status = :deliveredStatus
        )
    """)
    List<Shipment> findUndeliveredShipments(@Param("deliveredStatus") Status deliveredStatus);

    // Приходи за период (условие т.5.h)
    @Query("""
        SELECT COALESCE(SUM(s.price), 0) FROM Shipment s
        WHERE s.createdOn >= :from AND s.createdOn <= :to
    """)
    BigDecimal sumRevenueBetween(@Param("from") Instant from, @Param("to") Instant to);


}
