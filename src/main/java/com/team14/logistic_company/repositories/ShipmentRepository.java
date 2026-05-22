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

/**
 * Repository interface used for shipment database operations.
 * Provides methods for searching shipments and generating reports.
 */
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    /**
     * Returns all shipments ordered by creation date in descending order.
     *
     * @return list of shipments
     */
    List<Shipment> findAllByOrderByCreatedOnDesc();

    /**
     * Returns all shipments registered by a specific employee.
     *
     * @param employeeId the employee identifier
     * @return list of shipments
     */
    List<Shipment> findAllByEmployeeId(Integer employeeId);

    /**
     * Finds a shipment by unique tracking identifier.
     *
     * @param uniqueId shipment tracking identifier
     * @return optional containing the shipment if found
     */
    Optional<Shipment> findByUniqueId(String uniqueId);

    /**
     * Returns all shipments sent by a specific client.
     *
     * @param userId the sender user identifier
     * @return list of shipments
     */
    List<Shipment> findAllBySender_User_Id(Integer userId);

    /**
     * Returns all shipments received by a specific client.
     *
     * @param userId the recipient user identifier
     * @return list of shipments
     */
    List<Shipment> findAllByRecipient_User_Id(Integer userId);

    /**
     * Returns all shipments that are not delivered.
     *
     * @param deliveredStatus delivered shipment status
     * @return list of undelivered shipments
     */
    @Query("""
        SELECT s FROM Shipment s
        WHERE s.id NOT IN (
            SELECT ss.shipment.id FROM ShipmentStatus ss
            WHERE ss.status = :deliveredStatus
        )
    """)
    List<Shipment> findUndeliveredShipments(@Param("deliveredStatus") Status deliveredStatus);

    /**
     * Calculates total shipment revenue for a given time period.
     *
     * @param from start date
     * @param to end date
     * @return total revenue
     */
    @Query("""
        SELECT COALESCE(SUM(s.price), 0) FROM Shipment s
        WHERE s.createdOn >= :from AND s.createdOn <= :to
    """)
    BigDecimal sumRevenueBetween(@Param("from") Instant from, @Param("to") Instant to);

    /**
     * Returns all shipments assigned to a specific office,
     * ordered by creation date in descending order.
     *
     * @param officeId the office identifier
     * @return list of shipments
     */
    List<Shipment> findAllByOffice_IdOrderByCreatedOnDesc(Integer officeId);
}