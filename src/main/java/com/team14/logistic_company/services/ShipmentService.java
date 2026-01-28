package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.ShipmentDto;
import com.team14.logistic_company.entities.*;
import com.team14.logistic_company.entities.enums.DeliveryType;
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
import java.util.List;

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


    ///TODO: change when the ui is ready
    private static final BigDecimal BASE_PRICE_TO_OFFICE = BigDecimal.valueOf(5.0);
    private static final BigDecimal BASE_PRICE_TO_ADDRESS = BigDecimal.valueOf(10.0);
    private static final BigDecimal PRICE_PER_KILO = BigDecimal.valueOf(2.0);



    @Override
    public List<Shipment> getAllShipmentsForEmployee(Authentication authentication) throws UnauthorizedAccess {
        requireEmployee(authentication);
        return shipmentRepository.findAllByOrderByCreatedOnDesc();
    }

    @Override
    public List<Shipment> getShipmentsByEmployeeId(Integer employeeId) {
        return shipmentRepository.findAllByEmployeeId(employeeId);
    }

    @Override
    public List<Shipment> getUndeliveredShipments() {
        return shipmentRepository.findUndeliveredShipments(Status.DELIVERED);
    }

    private BigDecimal calculatePrice(Shipment shipment) {
        BigDecimal base = shipment.getDeliveryType() == DeliveryType.TO_OFFICE
                ? BASE_PRICE_TO_OFFICE
                : BASE_PRICE_TO_ADDRESS;

        BigDecimal weightPart = PRICE_PER_KILO.multiply(BigDecimal.valueOf(shipment.getWeight()));
        return base.add(weightPart);
    }

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

    @Override
    public List<Shipment> getShipmentsForClient(Authentication authentication) throws UnauthorizedAccess {
        requireClient(authentication);

        User user = getUserFromAuthentication(authentication);
        Integer userId = user.getId();

        List<Shipment> sent = shipmentRepository.findAllBySender_User_Id(userId);
        List<Shipment> received = shipmentRepository.findAllByRecipient_User_Id(userId);

        return List.copyOf(
                new java.util.LinkedHashSet<Shipment>() {{
                    addAll(sent);
                    addAll(received);
                }}
        );
    }

    @Override
    public Shipment updateShipmentStatus(Integer shipmentId, Status newStatus) throws ShipmentNotFound {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + shipmentId));

        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(newStatus);
        shipmentStatusRepository.save(shipmentStatus);

        return shipment;
    }

    @Override
    public List<ShipmentStatus> getShipmentHistory(String uniqueID) throws ShipmentNotFound {
        Shipment shipment = shipmentRepository.findByUniqueId(uniqueID)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with unique ID: " + uniqueID));

        return shipmentStatusRepository.findByShipment_IdOrderByCreatedOnDesc(shipment.getId());
    }


    public List<ShipmentDto> findAllForView(Authentication authentication) {
        List<Shipment> shipments;
        if (isEmployee(authentication)) {
            shipments = shipmentRepository.findAllByOrderByCreatedOnDesc();
        } else {
            shipments = getShipmentsForClient(authentication);
        }
        return shipments.stream().map(this::toDtoWithCurrentStatus).toList();
    }

    public ShipmentDto findByIdForView(Integer id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + id));
        return toDtoWithCurrentStatus(shipment);
    }

    public ShipmentDto createFromDto(ShipmentDto dto, Authentication authentication) {
        requireEmployee(authentication);

        Shipment shipment = new Shipment();

        shipment.setEmployee(employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId())));

        shipment.setSender(clientRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + dto.getSenderId())));

        shipment.setRecipient(clientRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found: " + dto.getRecipientId())));

        shipment.setSenderAddress(addressRepository.findById(dto.getSenderAddressId())
                .orElseThrow(() -> new RuntimeException("Sender address not found: " + dto.getSenderAddressId())));

        if (dto.getRecipientAddressId() != null) {
            shipment.setRecipientAddress(addressRepository.findById(dto.getRecipientAddressId())
                    .orElseThrow(() -> new RuntimeException("Recipient address not found: " + dto.getRecipientAddressId())));
        } else {
            shipment.setRecipientAddress(null);
        }
        shipment.setDeliveryType(dto.getDeliveryType());

        shipment.setOffice(officeRepository.findById(dto.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office not found: " + dto.getOfficeId())));

        shipment.setWeight(dto.getWeight());
        shipment.setUniqueId(dto.getUniqueId());
        shipment.setPrice(null);

        Shipment saved = registerShipment(shipment, authentication);
        return toDtoWithCurrentStatus(saved);
    }


    // --------- helpers ---------

    private ShipmentDto toDtoWithCurrentStatus(Shipment s) {
        ShipmentDto dto = new ShipmentDto();
        dto.setId(s.getId());
        dto.setEmployeeId(s.getEmployee() != null ? s.getEmployee().getId() : null);
        dto.setSenderId(s.getSender() != null ? s.getSender().getId() : null);
        dto.setRecipientId(s.getRecipient() != null ? s.getRecipient().getId() : null);
        dto.setSenderAddressId(s.getSenderAddress() != null ? s.getSenderAddress().getId() : null);
        dto.setRecipientAddressId(s.getRecipientAddress() != null ? s.getRecipientAddress().getId() : null);
        dto.setOfficeId(s.getOffice() != null ? s.getOffice().getId() : null);
        dto.setWeight(s.getWeight());
        dto.setPrice(s.getPrice());
        dto.setUniqueId(s.getUniqueId());
        dto.setCreatedOn(s.getCreatedOn());
        dto.setUpdatedOn(s.getUpdatedOn());

        // “текущ статус” = последния запис по createdOn
        ShipmentStatus last = shipmentStatusRepository.findByShipment_IdOrderByCreatedOnDesc(s.getId())
                .stream()
                .findFirst()
                .orElse(null);

        dto.setCurrentStatus(last != null ? last.getStatus().name() : null);
        return dto;
    }

    private void validateShipment(Shipment shipment) {
        if (shipment == null) throw new IllegalArgumentException("Shipment cannot be null.");
        if (shipment.getWeight() <= 0) throw new IllegalArgumentException("Weight must be a positive value.");
        if (shipment.getSender() == null || shipment.getRecipient() == null)
            throw new IllegalArgumentException("Sender and recipient must be provided.");
        if (shipment.getSenderAddress() == null) {
            throw new IllegalArgumentException("Sender address must be provided.");
        }
        if (shipment.getOffice() == null) throw new IllegalArgumentException("Office must be provided.");
        if (shipment.getUniqueId() == null || shipment.getUniqueId().isBlank())
            throw new IllegalArgumentException("UniqueId must be provided.");
        if (shipment.getDeliveryType() == null) {
            throw new IllegalArgumentException("DeliveryType must be provided.");
        }
        if (shipment.getDeliveryType() == DeliveryType.TO_ADDRESS && shipment.getRecipientAddress() == null) {
            throw new IllegalArgumentException("Recipient address is required for TO_ADDRESS delivery.");
        }
    }

    private boolean isEmployee(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.EMPLOYEE.name()) || a.getAuthority().equals(Role.ADMIN.name()));
    }

    private void requireEmployee(Authentication authentication) {
        if (!isEmployee(authentication)) {
            throw new UnauthorizedAccess("Access denied. Only employees can perform this action.");
        }
    }

    private void requireClient(Authentication authentication) {
        boolean ok = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.CLIENT.name()));
        if (!ok) throw new UnauthorizedAccess("Access denied. Only clients can perform this action.");
    }

    private User getUserFromAuthentication(Authentication authentication) throws UserNotFound {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFound("User not found with username: " + authentication.getName()));
    }

    public List<Shipment> getSentByClient(Authentication authentication) {
        requireClient(authentication);
        Integer userId = getUserFromAuthentication(authentication).getId();
        return shipmentRepository.findAllBySender_User_Id(userId);
    }

    public List<Shipment> getReceivedByClient(Authentication authentication) {
        requireClient(authentication);
        Integer userId = getUserFromAuthentication(authentication).getId();

        return shipmentRepository.findAllByRecipient_User_Id(userId).stream()
                .filter(s -> getLastStatusOrNull(s.getId()) == Status.DELIVERED)
                .toList();
    }

    public List<Shipment> getExpectedByClient(Authentication authentication) {
        requireClient(authentication);
        Integer userId = getUserFromAuthentication(authentication).getId();

        return shipmentRepository.findAllByRecipient_User_Id(userId).stream()
                .filter(s -> {
                    Status last = getLastStatusOrNull(s.getId());
                    return last == null || last != Status.DELIVERED;
                })
                .toList();
    }

    private Status getLastStatusOrNull(Integer shipmentId) {
        return shipmentStatusRepository.findByShipment_IdOrderByCreatedOnDesc(shipmentId)
                .stream()
                .findFirst()
                .map(ShipmentStatus::getStatus)
                .orElse(null);
    }
    public BigDecimal getRevenueBetween(Instant from, Instant to, Authentication authentication) {
        requireEmployee(authentication);
        return shipmentRepository.sumRevenueBetween(from, to);
    }
    public ShipmentDto findByIdForEdit(Integer id, Authentication authentication) {
        requireEmployee(authentication);
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + id));
        return toDtoWithCurrentStatus(shipment);
    }

    public ShipmentDto updateFromDto(Integer id, ShipmentDto dto, Authentication authentication) {
        requireEmployee(authentication);

        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFound("Shipment not found with ID: " + id));

        shipment.setEmployee(employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId())));

        shipment.setSender(clientRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + dto.getSenderId())));

        shipment.setRecipient(clientRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found: " + dto.getRecipientId())));

        shipment.setSenderAddress(addressRepository.findById(dto.getSenderAddressId())
                .orElseThrow(() -> new RuntimeException("Sender address not found: " + dto.getSenderAddressId())));

        if (dto.getRecipientAddressId() != null) {
            shipment.setRecipientAddress(addressRepository.findById(dto.getRecipientAddressId())
                    .orElseThrow(() -> new RuntimeException("Recipient address not found: " + dto.getRecipientAddressId())));
        } else {
            shipment.setRecipientAddress(null);
        }

        shipment.setDeliveryType(dto.getDeliveryType());

        shipment.setOffice(officeRepository.findById(dto.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office not found: " + dto.getOfficeId())));

        shipment.setWeight(dto.getWeight());

        // валидирай и преизчисли цената
        validateShipment(shipment);
        shipment.setPrice(calculatePrice(shipment));

        Shipment saved = shipmentRepository.save(shipment);
        return toDtoWithCurrentStatus(saved);
    }
    public void deleteShipment(Integer id, Authentication authentication) {
        requireEmployee(authentication);

        if (!shipmentRepository.existsById(id)) {
            throw new ShipmentNotFound("Shipment not found with ID: " + id);
        }

        shipmentStatusRepository.deleteByShipment_Id(id);
        shipmentRepository.deleteById(id);
    }


}
