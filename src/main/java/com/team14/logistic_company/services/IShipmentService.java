package com.team14.logistic_company.services;

import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.ShipmentStatus;
import com.team14.logistic_company.entities.enums.Status;
import com.team14.logistic_company.services.exceptions.ShipmentNotFound;
import com.team14.logistic_company.services.exceptions.UnauthorizedAccess;
import com.team14.logistic_company.services.exceptions.UserNotFound;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IShipmentService {
    List<Shipment> getAllShipmentsForEmployee(Authentication authentication) throws UnauthorizedAccess;

    List<Shipment> getShipmentsByEmployeeId(Integer employeeId);

    List<Shipment> getUndeliveredShipments();

    Shipment registerShipment(Shipment shipment, Authentication authentication)
            throws UnauthorizedAccess, UserNotFound;

    List<Shipment> getShipmentsForClient(Authentication authentication) throws UnauthorizedAccess;

    Shipment updateShipmentStatus(Integer shipmentId, Status newStatus) throws ShipmentNotFound;

    List<ShipmentStatus> getShipmentHistory(String uniqueID) throws ShipmentNotFound;
}
