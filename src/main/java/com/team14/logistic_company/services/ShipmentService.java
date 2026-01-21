package com.team14.logistic_company.services;

import com.team14.logistic_company.entities.Shipment;
import com.team14.logistic_company.entities.ShipmentStatus;
import com.team14.logistic_company.repositories.ShipmentRepository;
import com.team14.logistic_company.services.exceptions.ShipmentNotFound;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
/// TODO: it hat to have implements IShipmentService
/// TODO: finish and fix the class
public class ShipmentService  {

    /*@Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShipmentStatusRepository shipmentStatusRepository;

    @Autowired
    private UserRepository userRepository;*/

    private static final double BASE_PRICE_TO_OFFICE = 5.0;
    private static final double BASE_PRICE_TO_ADDRESS = 10.0;
    private static final double PRICE_PER_KILO = 2.0;

    /**
     * Retrieves all shipments accessible to employees.
     *
     * @param authentication The authentication object containing user details.
     * @return List of all shipments.
     * @throws UnauthorizedAccess If the user is not an employee.
     *
    public List<Shipment> getAllShipmentsForEmployee(Authentication authentication) {
        if (isEmployee(authentication)) {
            return shipmentRepository.findAll();
        }
        throw new UnauthorizedAccess("Access denied. Only employees can view all shipments.");
    }*/

    /**
     * Retrieves all shipments registered by a specific employee.
     *
     * @param employeeId The ID of the employee.
     * @return List of shipments registered by the employee.
     *
    public List<Shipment> getShipmentsByEmployeeId(Integer employeeId) {
        return shipmentRepository.findAllByEmployeeId(employeeId);
    }*/

    /**
     * Retrieves all shipments that have not been delivered.
     *
     * @return List of undelivered shipments.

    public List<Shipment> getUndeliveredShipments() {
        return shipmentRepository.findUndeliveredShipments(Status.DELIVERED);
    }*/

    /**
     * Registers a new shipment in the system.
     *
     * @param shipment       The shipment entity to register.
     * @param authentication The authentication object containing user details.
     * @return The registered shipment.
     * @throws UnauthorizedAccess If the user is not an employee.

    public Shipment registerShipment(Shipment shipment, Authentication authentication) {
        if (!isEmployee(authentication)) {
            throw new UnauthorizedAccess("Only employees can register shipments.");
        }
        validateShipment(shipment);
        return shipmentRepository.save(shipment);
    }*/

    /**
     * Retrieves shipments sent or received by a client.
     *
     * @param authentication The authentication object containing user details.
     * @return List of shipments related to the client.
     * @throws UnauthorizedAccess If the user is not a client.

    public List<Shipment> getShipmentsForClient(Authentication authentication) {
        if (isClient(authentication)) {
            Integer userId = getUserIdFromAuthentication(authentication);
            return shipmentRepository.findAll().stream()
                    .filter(s -> s.getSender().getUser().getId().equals(userId)
                            || s.getRecipient().getUser().getId().equals(userId))
                    .toList();
        }
        throw new UnauthorizedAccess("Access denied. Only clients can view their shipments.");
    }*/

    /**
     * Validates the shipment details.
     *
     * @param shipment The shipment entity to validate.
     * @throws IllegalArgumentException If the shipment details are invalid.

    private void validateShipment(Shipment shipment) {
        if (shipment.getWeight() <= 0) {
            throw new IllegalArgumentException("Weight must be a positive value.");
        }
        if (shipment.getSenderAddress() == null || shipment.getRecipientAddress() == null) {
            throw new IllegalArgumentException("Sender and recipient addresses must be provided.");
        }
    }*/

    /**
     * Checks if the user is an employee.
     *
     * @param authentication The authentication object containing user details.
     * @return True if the user is an employee, false otherwise.

    private boolean isEmployee(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.EMPLOYEE.name()));
    }*/

    /**
     * Checks if the user is a client.
     *
     * @param authentication The authentication object containing user details.
     * @return True if the user is a client, false otherwise.

    private boolean isClient(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.CLIENT.name()));
    }*/

    /**
     * Extracts the user ID from the authentication object.
     *
     * @param authentication The authentication object containing user details.
     * @return The user ID.

    private Integer getUserIdFromAuthentication(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName());
        return user.getId();
    }*/

    /**
     * Calculates the price of the shipment.
     *
     * @param shipment The shipment entity.
     * @return The calculated price.

    private double calculatePrice(Shipment shipment) {
        double basePrice = shipment.getRecipientAddress() == null ? BASE_PRICE_TO_OFFICE : BASE_PRICE_TO_ADDRESS;
        return basePrice + (shipment.getWeight() * PRICE_PER_KILO);
    }*/

    /**
     * Updates the status of a shipment.
     *
     * @param shipmentId The ID of the shipment.
     * @param newStatus  The new status to set.
     * @return The updated shipment.
     * @throws ShipmentNotFound If the shipment is not found.

    public Shipment updateShipmentStatus(Integer shipmentId, Status newStatus) {

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + shipmentId));

        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(newStatus);
        shipmentStatusRepository.save(shipmentStatus);

        return shipment;
    }*/

    /**
     * Retrieves the history of a shipment's statuses.
     *
     * @param shipmentId The ID of the shipment.
     * @return List of shipment statuses.
     * @throws ShipmentNotFound If the shipment is not found.

    public List<ShipmentStatus> getShipmentHistory(String uniqueId) {
        Shipment shipment = shipmentRepository.findByUniqueId(uniqueId);
        return shipmentStatusRepository.findByShipmentId(shipment.getId());
    }*/

    /**
     * Retrieves shipment statistics.
     *
     * @return A map containing total shipments, delivered shipments, total weight,
     *         and total revenue.

    public Map<String, Object> getShipmentStatistics() {
        long totalShipments = shipmentRepository.count();
        long deliveredShipments = shipmentStatusRepository.findAll().stream()
                .filter(shipmentStatus -> shipmentStatus.getStatus() == Status.DELIVERED)
                .count();
        double totalWeight = shipmentRepository.findAll().stream()
                .mapToDouble(Shipment::getWeight)
                .sum();

        BigDecimal totalRevenue = shipmentRepository.findAll().stream()
                .map(Shipment::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalShipments", totalShipments);
        stats.put("deliveredShipments", deliveredShipments);
        stats.put("totalWeight", totalWeight);
        stats.put("totalRevenue", totalRevenue);

        return stats;
    }*/

    /**
     * Retrieves shipments filtered by country and city.
     *
     * @param countryName The name of the country.
     * @param cityName    The name of the city.
     * @return List of shipments filtered by the specified location.

    public List<Shipment> getShipmentsByCountryAndCity(String countryName, String cityName) {
        return shipmentRepository.findAll().stream()
                .filter(shipment -> shipment.getSenderAddress().getCity().getName().equals(cityName) &&
                        shipment.getSenderAddress().getCity().getCountry().getName().equals(countryName))
                .toList();
    }
*//*
    // Get all Shipments
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    // Get a Shipment by ID
    public Optional<Shipment> getShipmentById(Integer id) {
        return shipmentRepository.findById(id);
    }

    // Update Shipment and its Status
    public Shipment updateShipment(Integer id, Shipment updatedShipment, ShipmentStatus updatedStatus) {
        if (shipmentRepository.existsById(id)) {
            updatedShipment.setId(id);
            Shipment updatedShipmentEntity = shipmentRepository.save(updatedShipment);

            // Update shipment status
            updatedStatus.setShipment(updatedShipmentEntity);
            shipmentStatusRepository.save(updatedStatus);

            return updatedShipmentEntity;
        }
        return null;
    }
*/
    /**
     * Deletes a shipment from the system by its ID.
     *
     * @param shipmentId The ID of the shipment to delete.
     * @throws ShipmentNotFound If the shipment with the specified ID does not
     *                          exist.

    public void deleteShipment(Integer shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + shipmentId));
        shipmentRepository.delete(shipment);
    }
*/
}
