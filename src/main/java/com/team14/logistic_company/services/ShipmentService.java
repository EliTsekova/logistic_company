package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.*;
import com.team14.logistic_company.entities.enums.DeliveryType;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.entities.enums.Status;
import com.team14.logistic_company.repositories.*;
import com.team14.logistic_company.services.exceptions.ShipmentNotFound;
import com.team14.logistic_company.services.exceptions.UnauthorizedAccess;
import com.team14.logistic_company.services.exceptions.UserNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
/**
 * Service class responsible for managing shipments in the logistic company system.
 *
 * <p>This service implements all business logic related to shipment processing,
 * including registration, pricing, status tracking, filtering, and role-based access control.</p>
 *
 * <p>Key responsibilities include:</p>
 * <ul>
 *     <li>Registering new shipments and calculating their price</li>
 *     <li>Managing shipment statuses (submitted, processed, delivered, etc.)</li>
 *     <li>Filtering shipments based on employee, deliveryman, or status</li>
 *     <li>Ensuring authorization rules based on user roles</li>
 * </ul>
 *
 * <p>Access to methods is restricted based on authentication and employee roles:
 * employees can view all shipments, while deliverymen see only assigned ones.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentService implements IShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final OfficeRepository officeRepository;
    private final CityRepository cityRepository;

    private static final BigDecimal BASE_PRICE_TO_OFFICE = BigDecimal.valueOf(5.0);
    private static final BigDecimal BASE_PRICE_TO_ADDRESS = BigDecimal.valueOf(10.0);
    private static final BigDecimal PRICE_PER_KILO = BigDecimal.valueOf(2.0);

    /**
     * Retrieves all shipments accessible to the authenticated employee.
     *
     * <p>If the authenticated user is a deliveryman, only shipments assigned to that deliveryman
     * are returned. Otherwise, all shipments in the system are returned.</p>
     *
     * @param authentication the current authenticated user
     * @return list of shipments visible to the employee
     * @throws UnauthorizedAccess if the user is not an employee
     */
    @Override
    public List<Shipment> getAllShipmentsForEmployee(Authentication authentication) throws UnauthorizedAccess {
        requireEmployee(authentication);

        if (isDeliveryman(authentication)) {
            Employee employee = getEmployeeFromAuthentication(authentication);

            return shipmentRepository.findAllByOrderByCreatedOnDesc()
                    .stream()
                    .filter(s -> s.getDeliveryman() != null
                            && s.getDeliveryman().getId().equals(employee.getId()))
                    .toList();
        }

        return shipmentRepository.findAllByOrderByCreatedOnDesc();
    }

    /**
     * Retrieves all shipments registered by a specific employee.
     *
     * @param employeeId the ID of the employee
     * @return list of shipments created by the employee
     */
    @Override
    public List<Shipment> getShipmentsByEmployeeId(Integer employeeId) {
        return shipmentRepository.findAllByEmployeeId(employeeId);
    }
    /**
     * Retrieves all shipments assigned to a specific deliveryman.
     *
     * @param deliverymanId the ID of the deliveryman
     * @return list of assigned shipments
     */
    public List<Shipment> getShipmentsByDeliverymanId(Integer deliverymanId) {
        return shipmentRepository.findAllByOrderByCreatedOnDesc()
                .stream()
                .filter(s -> s.getDeliveryman() != null
                        && s.getDeliveryman().getId().equals(deliverymanId))
                .toList();
    }
    /**
     * Retrieves all shipments that are not yet delivered.
     *
     * @return list of shipments excluding those with DELIVERED status
     */
    @Override
    public List<Shipment> getUndeliveredShipments() {
        return shipmentRepository.findUndeliveredShipments(Status.DELIVERED);
    }
    /**
     * Retrieves all undelivered shipments assigned to a specific deliveryman.
     *
     * @param deliverymanId the ID of the deliveryman
     * @return list of undelivered shipments for that deliveryman
     */
    public List<Shipment> getUndeliveredShipmentsForDeliveryman(Integer deliverymanId) {
        return getUndeliveredShipments()
                .stream()
                .filter(s -> s.getDeliveryman() != null
                        && s.getDeliveryman().getId().equals(deliverymanId))
                .toList();
    }
    /**
     * Calculates the price of a shipment based on delivery type and weight.
     *
     * <p>Pricing rules:</p>
     * <ul>
     *     <li>Base price depends on delivery type (office or address)</li>
     *     <li>Additional cost is calculated per kilogram</li>
     * </ul>
     *
     * @param shipment the shipment entity
     * @return calculated price as BigDecimal
     */
    private BigDecimal calculatePrice(Shipment shipment) {
        BigDecimal base = shipment.getDeliveryType() == DeliveryType.TO_OFFICE
                ? BASE_PRICE_TO_OFFICE
                : BASE_PRICE_TO_ADDRESS;

        BigDecimal weightPart = PRICE_PER_KILO.multiply(BigDecimal.valueOf(shipment.getWeight()));
        return base.add(weightPart);
    }
    /**
     * Creates and persists a new address entity.
     *
     * @param cityId the ID of the city
     * @param street street name
     * @param postalCode postal code
     * @return saved Address entity
     * @throws IllegalArgumentException if any required field is missing
     */
    private Address createAddress(Integer cityId, String street, String postalCode) {
        if (cityId == null || street == null || street.isBlank() || postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Address data is required.");
        }

        Address address = new Address();

        address.setCity(cityRepository.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found: " + cityId)));

        address.setStreet(street);
        address.setPostalCode(postalCode);

        return addressRepository.save(address);
    }
    /**
     * Copies address data from a Shipment entity into a ShipmentDto for editing purposes.
     *
     * @param dto the DTO to be filled
     * @param shipment the shipment entity containing address data
     */
    private void fillAddressFieldsForEdit(ShipmentDto dto, Shipment shipment) {
        if (shipment.getSenderAddress() != null) {
            dto.setSenderCityId(
                    shipment.getSenderAddress().getCity() != null
                            ? shipment.getSenderAddress().getCity().getId()
                            : null
            );

            dto.setSenderStreet(shipment.getSenderAddress().getStreet());
            dto.setSenderPostalCode(shipment.getSenderAddress().getPostalCode());
        }

        if (shipment.getRecipientAddress() != null) {
            dto.setRecipientCityId(
                    shipment.getRecipientAddress().getCity() != null
                            ? shipment.getRecipientAddress().getCity().getId()
                            : null
            );

            dto.setRecipientStreet(shipment.getRecipientAddress().getStreet());
            dto.setRecipientPostalCode(shipment.getRecipientAddress().getPostalCode());
        }
    }
    /**
     * Registers a new shipment in the system.
     *
     * <p>The method performs the following operations:</p>
     * <ul>
     *     <li>Validates employee authorization</li>
     *     <li>Determines delivery type if not provided</li>
     *     <li>Validates shipment data</li>
     *     <li>Calculates shipment price based on weight and delivery type</li>
     *     <li>Saves shipment in the database</li>
     *     <li>Creates initial shipment status (SUBMITTED)</li>
     * </ul>
     *
     * @param shipment the shipment entity to register
     * @param authentication the authenticated employee performing the action
     * @return the saved shipment entity
     * @throws UnauthorizedAccess if user is not authorized
     * @throws UserNotFound if required user data is missing
     */
    @Override
    public Shipment registerShipment(Shipment shipment, Authentication authentication)
            throws UnauthorizedAccess, UserNotFound {

        requireEmployee(authentication);

        if (shipment.getDeliveryType() == null) {
            shipment.setDeliveryType(
                    shipment.getRecipientAddress() == null ? DeliveryType.TO_OFFICE : DeliveryType.TO_ADDRESS
            );
        }

        validateShipment(shipment);
        shipment.setPrice(calculatePrice(shipment));

        Shipment saved = shipmentRepository.save(shipment);

        ShipmentStatus initialStatus = new ShipmentStatus();
        initialStatus.setShipment(saved);
        initialStatus.setStatus(Status.SUBMITTED);
        initialStatus.setComment("Shipment registered");

        shipmentStatusRepository.save(initialStatus);

        return saved;
    }
    /**
     * Retrieves all shipments related to the authenticated client.
     *
     * <p>This includes both shipments where the client is the sender
     * and shipments where the client is the recipient.</p>
     *
     * <p>The results are merged and duplicates are removed while preserving order.</p>
     *
     * @param authentication the authenticated client
     * @return list of shipments associated with the client
     * @throws UnauthorizedAccess if the user is not a client
     */
    @Override
    public List<Shipment> getShipmentsForClient(Authentication authentication) throws UnauthorizedAccess {
        requireClient(authentication);

        Integer userId = getUserFromAuthentication(authentication).getId();

        List<Shipment> sent = shipmentRepository.findAllBySender_User_Id(userId);
        List<Shipment> received = shipmentRepository.findAllByRecipient_User_Id(userId);

        LinkedHashSet<Shipment> all = new LinkedHashSet<>();
        all.addAll(sent);
        all.addAll(received);

        return all.stream().toList();
    }
    /**
     * Updates the status of a shipment and creates a new shipment status entry.
     *
     * <p>Only employees are allowed to update shipment status. If the authenticated
     * user is a deliveryman, they may only update shipments assigned to them.</p>
     *
     * @param shipmentId the ID of the shipment
     * @param newStatus the new status to assign
     * @param authentication the authenticated user performing the operation
     * @return the updated shipment
     * @throws ShipmentNotFound if the shipment does not exist
     * @throws UnauthorizedAccess if the user is not allowed to modify the shipment
     */
    public Shipment updateShipmentStatus(Integer shipmentId,
                                         Status newStatus,
                                         Authentication authentication) throws ShipmentNotFound {

        requireEmployee(authentication);

        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + shipmentId));

        if (isDeliveryman(authentication)) {
            Employee deliveryman = getEmployeeFromAuthentication(authentication);

            if (shipment.getDeliveryman() == null ||
                    !shipment.getDeliveryman().getId().equals(deliveryman.getId())) {
                throw new UnauthorizedAccess("Куриерът може да променя само пратки, назначени на него.");
            }
        }

        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(newStatus);

        shipmentStatusRepository.save(shipmentStatus);

        return shipment;
    }
    /**
     * Retrieves the full status history of a shipment by its unique tracking ID.
     *
     * <p>The history is returned in descending order by creation date.</p>
     *
     * @param uniqueID the unique tracking identifier of the shipment
     * @return list of all status changes for the shipment
     * @throws ShipmentNotFound if no shipment exists with the given unique ID
     */
    @Override
    public List<ShipmentStatus> getShipmentHistory(String uniqueID) throws ShipmentNotFound {
        Shipment shipment = shipmentRepository.findByUniqueId(uniqueID)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with unique ID: " + uniqueID));

        return shipmentStatusRepository.findByShipment_IdOrderByCreatedOnDesc(shipment.getId());
    }
    /**
     * Retrieves all shipments formatted for view based on the role of the authenticated user.
     *
     * <p>Role-based access rules:</p>
     * <ul>
     *     <li>Deliveryman → only assigned shipments</li>
     *     <li>Employee → all shipments</li>
     *     <li>Client → only shipments where they are sender or recipient</li>
     * </ul>
     *
     * @param authentication the authenticated user
     * @return list of shipment DTOs with current status information
     */
    public List<ShipmentDto> findAllForView(Authentication authentication) {
        List<Shipment> shipments;

        if (isDeliveryman(authentication)) {
            Employee deliveryman = getEmployeeFromAuthentication(authentication);

            shipments = shipmentRepository.findAllByOrderByCreatedOnDesc()
                    .stream()
                    .filter(s -> s.getDeliveryman() != null
                            && s.getDeliveryman().getId().equals(deliveryman.getId()))
                    .toList();

        } else if (isEmployee(authentication)) {
            shipments = shipmentRepository.findAllByOrderByCreatedOnDesc();
        } else {
            shipments = getShipmentsForClient(authentication);
        }

        return shipments.stream()
                .map(this::toDtoWithCurrentStatus)
                .toList();
    }
    /**
     * Retrieves a single shipment for view with strict role-based access control.
     *
     * <p>Access rules:</p>
     * <ul>
     *     <li>Deliveryman → only if assigned to the shipment</li>
     *     <li>Employee → full access</li>
     *     <li>Client → only if sender or recipient</li>
     * </ul>
     *
     * @param id the shipment ID
     * @param authentication the authenticated user
     * @return shipment DTO with detailed information
     * @throws ShipmentNotFound if shipment does not exist
     * @throws UnauthorizedAccess if user has no access to the shipment
     */
    public ShipmentDto findByIdForView(Integer id, Authentication authentication) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + id));

        if (isDeliveryman(authentication)) {
            Employee deliveryman = getEmployeeFromAuthentication(authentication);

            if (shipment.getDeliveryman() == null ||
                    !shipment.getDeliveryman().getId().equals(deliveryman.getId())) {
                throw new UnauthorizedAccess("Нямате достъп до тази пратка.");
            }

            return toDtoWithCurrentStatus(shipment);
        }

        if (isEmployee(authentication)) {
            return toDtoWithCurrentStatus(shipment);
        }

        requireClient(authentication);

        Integer userId = getUserFromAuthentication(authentication).getId();

        boolean isSender = shipment.getSender() != null
                && shipment.getSender().getUser() != null
                && shipment.getSender().getUser().getId().equals(userId);

        boolean isRecipient = shipment.getRecipient() != null
                && shipment.getRecipient().getUser() != null
                && shipment.getRecipient().getUser().getId().equals(userId);

        if (!isSender && !isRecipient) {
            throw new UnauthorizedAccess("Нямате достъп до тази пратка.");
        }

        return toDtoWithCurrentStatus(shipment);
    }
    /**
     * Creates a new shipment from a ShipmentDto and persists it in the system.
     *
     * <p>This method performs full entity construction including:</p>
     * <ul>
     *     <li>Assigning employee and deliveryman</li>
     *     <li>Setting sender and recipient</li>
     *     <li>Creating sender and recipient addresses</li>
     *     <li>Assigning office and delivery type</li>
     *     <li>Calculating weight and unique ID</li>
     * </ul>
     *
     * <p>After creation, the shipment is processed through registerShipment()
     * which handles validation, pricing, and initial status creation.</p>
     *
     * @param dto shipment data transfer object
     * @param authentication authenticated employee creating the shipment
     * @return created shipment as DTO
     * @throws UnauthorizedAccess if user is not an employee
     */
    public ShipmentDto createFromDto(ShipmentDto dto, Authentication authentication) {
        requireEmployee(authentication);

        Shipment shipment = new Shipment();

        shipment.setEmployee(employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId())));

        shipment.setDeliveryman(employeeRepository.findById(dto.getDeliverymanId())
                .orElseThrow(() -> new RuntimeException("Deliveryman not found: " + dto.getDeliverymanId())));

        shipment.setSender(clientRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + dto.getSenderId())));

        if (dto.getRecipientId() != null) {
            shipment.setRecipient(clientRepository.findById(dto.getRecipientId())
                    .orElseThrow(() -> new RuntimeException("Recipient not found: " + dto.getRecipientId())));
        } else {
            shipment.setRecipient(null);
        }

        shipment.setRecipientName(dto.getRecipientName());
        shipment.setRecipientPhone(dto.getRecipientPhone());

        shipment.setSenderAddress(createAddress(
                dto.getSenderCityId(),
                dto.getSenderStreet(),
                dto.getSenderPostalCode()
        ));

        shipment.setDeliveryType(dto.getDeliveryType());

        if (dto.getDeliveryType() == DeliveryType.TO_ADDRESS) {
            shipment.setRecipientAddress(createAddress(
                    dto.getRecipientCityId(),
                    dto.getRecipientStreet(),
                    dto.getRecipientPostalCode()
            ));
        } else {
            shipment.setRecipientAddress(null);
        }

        shipment.setOffice(officeRepository.findById(dto.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office not found: " + dto.getOfficeId())));

        shipment.setWeight(dto.getWeight());
        shipment.setUniqueId(dto.getUniqueId());
        shipment.setPrice(null);

        Shipment saved = registerShipment(shipment, authentication);

        return toDtoWithCurrentStatus(saved);
    }
    /**
     * Converts a Shipment entity into a ShipmentDto enriched with current status information.
     *
     * <p>This method includes:</p>
     * <ul>
     *     <li>Mapping of all related entity IDs</li>
     *     <li>Human-readable names for employees, clients, and offices</li>
     *     <li>Formatted address strings</li>
     *     <li>Current shipment status (latest entry)</li>
     * </ul>
     *
     * @param s the shipment entity
     * @return fully populated ShipmentDto
     */
    public ShipmentDto toDtoWithCurrentStatus(Shipment s) {
        ShipmentDto dto = new ShipmentDto();

        dto.setId(s.getId());

        dto.setEmployeeId(s.getEmployee() != null ? s.getEmployee().getId() : null);
        dto.setDeliverymanId(s.getDeliveryman() != null ? s.getDeliveryman().getId() : null);

        dto.setSenderId(s.getSender() != null ? s.getSender().getId() : null);
        dto.setRecipientId(s.getRecipient() != null ? s.getRecipient().getId() : null);

        dto.setSenderAddressId(s.getSenderAddress() != null ? s.getSenderAddress().getId() : null);
        dto.setRecipientAddressId(s.getRecipientAddress() != null ? s.getRecipientAddress().getId() : null);
        dto.setOfficeId(s.getOffice() != null ? s.getOffice().getId() : null);

        fillAddressFieldsForEdit(dto, s);

        dto.setEmployeeName(
                s.getEmployee() != null && s.getEmployee().getUser() != null
                        ? fullName(s.getEmployee().getUser())
                        : "Няма служител"
        );

        dto.setDeliverymanName(
                s.getDeliveryman() != null && s.getDeliveryman().getUser() != null
                        ? fullName(s.getDeliveryman().getUser())
                        : "Няма назначен куриер"
        );

        dto.setSenderName(
                s.getSender() != null && s.getSender().getUser() != null
                        ? fullName(s.getSender().getUser())
                        : "Няма подател"
        );

        dto.setRecipientName(
                s.getRecipientName() != null && !s.getRecipientName().isBlank()
                        ? s.getRecipientName()
                        : "Няма получател"
        );

        dto.setRecipientPhone(s.getRecipientPhone());

        dto.setSenderAddressText(
                s.getSenderAddress() != null
                        ? addressText(s.getSenderAddress())
                        : "Няма адрес"
        );

        dto.setRecipientAddressText(
                s.getRecipientAddress() != null
                        ? addressText(s.getRecipientAddress())
                        : "Доставка до офис"
        );

        dto.setOfficeTitle(
                s.getOffice() != null
                        ? s.getOffice().getTitle()
                        : "Няма офис"
        );

        dto.setWeight(s.getWeight());
        dto.setPrice(s.getPrice());
        dto.setDeliveryType(s.getDeliveryType());
        dto.setUniqueId(s.getUniqueId());
        dto.setCreatedOn(s.getCreatedOn());
        dto.setUpdatedOn(s.getUpdatedOn());

        ShipmentStatus last = shipmentStatusRepository
                .findByShipment_IdOrderByCreatedOnDesc(s.getId())
                .stream()
                .findFirst()
                .orElse(null);

        dto.setCurrentStatus(last != null ? last.getStatus().name() : null);

        return dto;
    }
    /**
     * Builds a full name string from a User entity.
     *
     * @param user the user entity
     * @return concatenated first name and last name
     */
    private String fullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
    /**
     * Formats an Address entity into a human-readable string.
     *
     * <p>Format: City, Street, Postal Code</p>
     *
     * @param address the address entity
     * @return formatted address string
     */
    private String addressText(Address address) {
        String cityName = address.getCity() != null ? address.getCity().getName() : "";
        return cityName + ", " + address.getStreet() + ", " + address.getPostalCode();
    }
    /**
     * Validates a shipment entity before persistence or update.
     *
     * <p>Ensures that all required fields are present and logically valid,
     * including employee, deliveryman, sender, recipient data, weight,
     * delivery type, and unique tracking ID.</p>
     *
     * @param shipment the shipment to validate
     * @throws IllegalArgumentException if any required field is missing or invalid
     */
    private void validateShipment(Shipment shipment) {
        if (shipment == null) {
            throw new IllegalArgumentException("Shipment cannot be null.");
        }

        if (shipment.getEmployee() == null) {
            throw new IllegalArgumentException("Employee must be provided.");
        }

        if (shipment.getDeliveryman() == null) {
            throw new IllegalArgumentException("Deliveryman must be provided.");
        }

        if (shipment.getWeight() <= 0) {
            throw new IllegalArgumentException("Weight must be a positive value.");
        }

        if (shipment.getSender() == null) {
            throw new IllegalArgumentException("Sender must be provided.");
        }

        if (shipment.getRecipientName() == null || shipment.getRecipientName().isBlank()) {
            throw new IllegalArgumentException("Recipient name must be provided.");
        }

        if (shipment.getRecipientPhone() == null || shipment.getRecipientPhone().isBlank()) {
            throw new IllegalArgumentException("Recipient phone must be provided.");
        }

        if (shipment.getSenderAddress() == null) {
            throw new IllegalArgumentException("Sender address must be provided.");
        }

        if (shipment.getOffice() == null) {
            throw new IllegalArgumentException("Office must be provided.");
        }

        if (shipment.getUniqueId() == null || shipment.getUniqueId().isBlank()) {
            throw new IllegalArgumentException("UniqueId must be provided.");
        }

        if (shipment.getDeliveryType() == null) {
            throw new IllegalArgumentException("DeliveryType must be provided.");
        }

        if (shipment.getDeliveryType() == DeliveryType.TO_ADDRESS && shipment.getRecipientAddress() == null) {
            throw new IllegalArgumentException("Recipient address is required for TO_ADDRESS delivery.");
        }
    }
    /**
     * Checks whether the authenticated user has employee or admin role.
     *
     * @param authentication the authentication object
     * @return true if user is EMPLOYEE or ADMIN, false otherwise
     */
    private boolean isEmployee(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.EMPLOYEE.name())
                        || a.getAuthority().equals(Role.ADMIN.name()));
    }
    /**
     * Determines whether the authenticated employee is a deliveryman.
     *
     * <p>Admin users are excluded from deliveryman role even if they have employee authority.</p>
     *
     * @param authentication the authentication object
     * @return true if the user is a deliveryman employee
     */
    private boolean isDeliveryman(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.name()));

        if (isAdmin) {
            return false;
        }

        boolean isEmployee = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.EMPLOYEE.name()));

        if (!isEmployee) {
            return false;
        }

        return getEmployeeFromAuthentication(authentication).getPositionType() == PositionType.DELIVERYMAN;
    }
    /**
     * Retrieves the Employee entity associated with the authenticated user.
     *
     * @param authentication the authentication object
     * @return the Employee entity linked to the current user
     * @throws RuntimeException if no employee is found for the username
     */
    private Employee getEmployeeFromAuthentication(Authentication authentication) {
        String username = authentication.getName();

        return employeeRepository.findAll()
                .stream()
                .filter(e -> e.getUser() != null
                        && e.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found for username: " + username));
    }
    /**
     * Ensures that the current user has employee privileges.
     *
     * @param authentication the authentication object
     * @throws UnauthorizedAccess if the user is not an employee
     */
    private void requireEmployee(Authentication authentication) {
        if (!isEmployee(authentication)) {
            throw new UnauthorizedAccess("Access denied. Only employees can perform this action.");
        }
    }
    /**
     * Ensures that the current user has client privileges.
     *
     * @param authentication the authentication object
     * @throws UnauthorizedAccess if the user is not a client
     */
    private void requireClient(Authentication authentication) {
        boolean ok = authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.CLIENT.name()));

        if (!ok) {
            throw new UnauthorizedAccess("Access denied. Only clients can perform this action.");
        }
    }
    /**
     * Retrieves the User entity based on authentication username.
     *
     * @param authentication the authentication object
     * @return the User entity
     * @throws UserNotFound if no user exists with the given username
     */
    private User getUserFromAuthentication(Authentication authentication) throws UserNotFound {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFound("User not found with username: " + authentication.getName()));
    }
    /**
     * Retrieves all shipments sent by the authenticated client.
     *
     * @param authentication the authenticated client
     * @return list of sent shipments
     * @throws UnauthorizedAccess if the user is not a client
     */
    public List<Shipment> getSentByClient(Authentication authentication) {
        requireClient(authentication);

        Integer userId = getUserFromAuthentication(authentication).getId();

        return shipmentRepository.findAllBySender_User_Id(userId);
    }
    /**
     * Retrieves all delivered shipments received by the authenticated client.
     *
     * <p>Only shipments with status DELIVERED are included.</p>
     *
     * @param authentication the authenticated client
     * @return list of received shipments
     * @throws UnauthorizedAccess if the user is not a client
     */
    public List<Shipment> getReceivedByClient(Authentication authentication) {
        requireClient(authentication);

        Integer userId = getUserFromAuthentication(authentication).getId();

        return shipmentRepository.findAllByRecipient_User_Id(userId)
                .stream()
                .filter(s -> getLastStatusOrNull(s.getId()) == Status.DELIVERED)
                .toList();
    }
    /**
     * Retrieves all shipments expected by the authenticated client.
     *
     * <p>Includes shipments that are not yet delivered.</p>
     *
     * @param authentication the authenticated client
     * @return list of pending shipments
     * @throws UnauthorizedAccess if the user is not a client
     */
    public List<Shipment> getExpectedByClient(Authentication authentication) {
        requireClient(authentication);

        Integer userId = getUserFromAuthentication(authentication).getId();

        return shipmentRepository.findAllByRecipient_User_Id(userId)
                .stream()
                .filter(s -> {
                    Status last = getLastStatusOrNull(s.getId());
                    return last == null || last != Status.DELIVERED;
                })
                .toList();
    }
    /**
     * Retrieves the latest status of a shipment.
     *
     * @param shipmentId the shipment ID
     * @return the last known status or null if no status exists
     */
    private Status getLastStatusOrNull(Integer shipmentId) {
        return shipmentStatusRepository.findByShipment_IdOrderByCreatedOnDesc(shipmentId)
                .stream()
                .findFirst()
                .map(ShipmentStatus::getStatus)
                .orElse(null);
    }
    /**
     * Calculates total revenue generated from shipments in a given time range.
     *
     * @param from start timestamp
     * @param to end timestamp
     * @param authentication the authenticated employee
     * @return total revenue as BigDecimal
     * @throws UnauthorizedAccess if the user is not an employee
     */
    public BigDecimal getRevenueBetween(Instant from, Instant to, Authentication authentication) {
        requireEmployee(authentication);

        return shipmentRepository.sumRevenueBetween(from, to);
    }
    /**
     * Retrieves a shipment for editing purposes.
     *
     * @param id shipment ID
     * @param authentication authenticated employee
     * @return shipment DTO
     * @throws UnauthorizedAccess if user is not an employee
     * @throws ShipmentNotFound if shipment does not exist
     */
    public ShipmentDto findByIdForEdit(Integer id, Authentication authentication) {
        requireEmployee(authentication);

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + id));

        return toDtoWithCurrentStatus(shipment);
    }
    /**
     * Updates an existing shipment using data from a DTO.
     *
     * <p>This method re-applies full business validation, recalculates price,
     * and persists the updated entity.</p>
     *
     * @param id shipment ID
     * @param dto updated shipment data
     * @param authentication authenticated employee
     * @return updated shipment DTO
     * @throws UnauthorizedAccess if user is not an employee
     * @throws ShipmentNotFound if shipment does not exist
     */
    public ShipmentDto updateFromDto(Integer id, ShipmentDto dto, Authentication authentication) {
        requireEmployee(authentication);

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + id));

        shipment.setEmployee(employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId())));

        shipment.setDeliveryman(employeeRepository.findById(dto.getDeliverymanId())
                .orElseThrow(() -> new RuntimeException("Deliveryman not found: " + dto.getDeliverymanId())));

        shipment.setSender(clientRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + dto.getSenderId())));

        if (dto.getRecipientId() != null) {
            shipment.setRecipient(clientRepository.findById(dto.getRecipientId())
                    .orElseThrow(() -> new RuntimeException("Recipient not found: " + dto.getRecipientId())));
        } else {
            shipment.setRecipient(null);
        }

        shipment.setRecipientName(dto.getRecipientName());
        shipment.setRecipientPhone(dto.getRecipientPhone());

        shipment.setSenderAddress(createAddress(
                dto.getSenderCityId(),
                dto.getSenderStreet(),
                dto.getSenderPostalCode()
        ));

        shipment.setDeliveryType(dto.getDeliveryType());

        if (dto.getDeliveryType() == DeliveryType.TO_ADDRESS) {
            shipment.setRecipientAddress(createAddress(
                    dto.getRecipientCityId(),
                    dto.getRecipientStreet(),
                    dto.getRecipientPostalCode()
            ));
        } else {
            shipment.setRecipientAddress(null);
        }

        shipment.setOffice(officeRepository.findById(dto.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office not found: " + dto.getOfficeId())));

        shipment.setWeight(dto.getWeight());

        validateShipment(shipment);
        shipment.setPrice(calculatePrice(shipment));

        Shipment saved = shipmentRepository.save(shipment);

        return toDtoWithCurrentStatus(saved);
    }
    /**
     * Deletes a shipment and its related status history.
     *
     * @param id shipment ID
     * @param authentication authenticated employee
     * @throws UnauthorizedAccess if user is not an employee
     * @throws ShipmentNotFound if shipment does not exist
     */
    public void deleteShipment(Integer id, Authentication authentication) {
        requireEmployee(authentication);

        if (!shipmentRepository.existsById(id)) {
            throw new ShipmentNotFound("Shipment not found with ID: " + id);
        }

        shipmentStatusRepository.deleteByShipment_Id(id);
        shipmentRepository.deleteById(id);
    }
    /**
     * Retrieves all shipments associated with a specific office.
     *
     * @param officeId office ID
     * @return list of shipments
     */
    public List<Shipment> getShipmentsByOfficeId(Integer officeId) {
        return shipmentRepository.findAllByOffice_IdOrderByCreatedOnDesc(officeId);
    }
    /**
     * Retrieves all shipments sent by a specific client.
     *
     * @param clientId client ID
     * @return list of shipments
     */
    public List<Shipment> getShipmentsByClientId(Integer clientId) {
        return shipmentRepository.findAll()
                .stream()
                .filter(shipment ->
                        shipment.getSender() != null
                                && shipment.getSender().getId().equals(clientId)
                )
                .toList();
    }
    /**
     * Retrieves all shipments sent by a specific client.
     *
     * <p>This method filters all shipments in the system and returns only those
     * where the given client is the sender.</p>
     *
     * <p>Note: This implementation loads all shipments from the database
     * and filters them in memory.</p>
     *
     * @param clientId the ID of the client
     * @return list of shipments sent by the specified client
     */
    public List<Shipment> getSentShipmentsByClientId(Integer clientId) {
        return shipmentRepository.findAll()
                .stream()
                .filter(shipment ->
                        shipment.getSender() != null
                                && shipment.getSender().getId().equals(clientId)
                )
                .toList();
    }
    /**
     * Retrieves all delivered shipments received by a specific client.
     *
     * @param clientId client ID
     * @return list of delivered shipments
     */
    public List<Shipment> getReceivedShipmentsByClientId(Integer clientId) {
        return shipmentRepository.findAll()
                .stream()
                .filter(shipment ->
                        shipment.getRecipient() != null
                                && shipment.getRecipient().getId().equals(clientId)
                )
                .filter(shipment -> getLastStatusOrNull(shipment.getId()) == Status.DELIVERED)
                .toList();
    }
    /**
     * Retrieves all non-delivered shipments for a specific client.
     *
     * @param clientId client ID
     * @return list of pending shipments
     */
    public List<Shipment> getExpectedShipmentsByClientId(Integer clientId) {
        return shipmentRepository.findAll()
                .stream()
                .filter(shipment ->
                        shipment.getRecipient() != null
                                && shipment.getRecipient().getId().equals(clientId)
                )
                .filter(shipment -> {
                    Status lastStatus = getLastStatusOrNull(shipment.getId());
                    return lastStatus == null || lastStatus != Status.DELIVERED;
                })
                .toList();
    }
    /**
     * Calculates shipment price based on weight and delivery type.
     *
     * @param weight shipment weight in kilograms
     * @param deliveryType type of delivery (TO_OFFICE or TO_ADDRESS)
     * @return calculated price
     * @throws IllegalArgumentException if weight is not positive
     */
    public BigDecimal calculatePrice(double weight, DeliveryType deliveryType) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than 0.");
        }

        BigDecimal base = deliveryType == DeliveryType.TO_OFFICE
                ? BASE_PRICE_TO_OFFICE
                : BASE_PRICE_TO_ADDRESS;

        BigDecimal weightPart = PRICE_PER_KILO.multiply(BigDecimal.valueOf(weight));

        return base.add(weightPart);
    }
}