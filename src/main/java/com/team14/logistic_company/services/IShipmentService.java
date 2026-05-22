/**
 * Service interface for managing Shipment operations.
 *
 * <p>
 * This is the most complex service in the system and handles:
 * shipment creation, updates, tracking, reporting,
 * and role-based access control (employees vs clients).
 * </p>
 *
 * <p>
 * Many methods require Spring Security Authentication
 * to enforce access rules depending on user role.
 * </p>
 */
package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.ShipmentStatus;
import com.team14.logistic_company.entities.enums.Status;
import com.team14.logistic_company.services.exceptions.ShipmentNotFound;
import com.team14.logistic_company.services.exceptions.UnauthorizedAccess;
import com.team14.logistic_company.services.exceptions.UserNotFound;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface IShipmentService {

    /**
     * Returns all shipments visible to an authenticated employee.
     *
     * @param authentication current authenticated user
     * @return list of shipments
     * @throws UnauthorizedAccess if user is not employee
     */
    List<Shipment> getAllShipmentsForEmployee(Authentication authentication)
            throws UnauthorizedAccess;

    /**
     * Retrieves shipments assigned to a specific employee.
     *
     * @param employeeId employee identifier
     * @return list of shipments
     */
    List<Shipment> getShipmentsByEmployeeId(Integer employeeId);

    /**
     * Retrieves all undelivered shipments.
     *
     * @return list of shipments not yet delivered
     */
    List<Shipment> getUndeliveredShipments();

    /**
     * Registers a new shipment in the system.
     *
     * @param shipment shipment entity
     * @param authentication current user
     * @return created shipment
     * @throws UnauthorizedAccess if user has no permission
     * @throws UserNotFound if sender/receiver user is invalid
     */
    Shipment registerShipment(Shipment shipment, Authentication authentication)
            throws UnauthorizedAccess, UserNotFound;

    /**
     * Retrieves shipments visible to a client user.
     *
     * @param authentication current authenticated client
     * @return list of shipments
     */
    List<Shipment> getShipmentsForClient(Authentication authentication);

    /**
     * Updates shipment status.
     *
     * @param shipmentId shipment identifier
     * @param newStatus new status
     * @param authentication current user
     * @return updated shipment
     * @throws ShipmentNotFound if shipment does not exist
     */
    Shipment updateShipmentStatus(Integer shipmentId,
                                  Status newStatus,
                                  Authentication authentication)
            throws ShipmentNotFound;

    /**
     * Retrieves full history of a shipment by tracking number.
     *
     * @param uniqueID shipment unique tracking ID
     * @return list of status changes
     * @throws ShipmentNotFound if shipment does not exist
     */
    List<ShipmentStatus> getShipmentHistory(String uniqueID)
            throws ShipmentNotFound;

    /**
     * Returns all shipments formatted for UI view.
     */
    List<ShipmentDto> findAllForView(Authentication authentication);

    /**
     * Finds shipment by id for view purposes.
     */
    ShipmentDto findByIdForView(Integer id, Authentication authentication);

    /**
     * Creates shipment from DTO input.
     */
    ShipmentDto createFromDto(ShipmentDto dto, Authentication authentication);

    /**
     * Retrieves shipment for editing.
     */
    ShipmentDto findByIdForEdit(Integer id, Authentication authentication);

    /**
     * Updates shipment from DTO input.
     */
    ShipmentDto updateFromDto(Integer id,
                              ShipmentDto dto,
                              Authentication authentication);

    /**
     * Deletes a shipment.
     */
    void deleteShipment(Integer id, Authentication authentication);

    /**
     * Returns shipments sent by the logged-in client.
     */
    List<Shipment> getSentByClient(Authentication authentication);

    /**
     * Returns shipments received by the logged-in client.
     */
    List<Shipment> getReceivedByClient(Authentication authentication);

    /**
     * Returns expected shipments for the logged-in client.
     */
    List<Shipment> getExpectedByClient(Authentication authentication);

    /**
     * Calculates revenue between two timestamps.
     */
    BigDecimal getRevenueBetween(Instant from,
                                 Instant to,
                                 Authentication authentication);

    List<Shipment> getShipmentsByOfficeId(Integer officeId);
    List<Shipment> getShipmentsByClientId(Integer clientId);
    List<Shipment> getSentShipmentsByClientId(Integer clientId);
    List<Shipment> getReceivedShipmentsByClientId(Integer clientId);
    List<Shipment> getExpectedShipmentsByClientId(Integer clientId);
}