package com.team14.logistic_company.entities;

import com.team14.logistic_company.entities.enums.DeliveryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entity representing a shipment in the logistics company system.
 *
 * A shipment contains information about sender, recipient,
 * delivery type, assigned employees, delivery office,
 * addresses, weight, and calculated price.
 */
@Getter
@Setter
@Entity
public class Shipment {

    /**
     * Primary key of the shipment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Office employee or coordinator who registered the shipment.
     */
    @ManyToOne
    @JoinColumn(name = "EmployeeId", nullable = false)
    private Employee employee;

    /**
     * Courier responsible for delivering the shipment.
     */
    @ManyToOne
    @JoinColumn(name = "DeliverymanId", nullable = true)
    private Employee deliveryman;

    /**
     * Registered recipient client.
     *
     * This field is optional because shipments can also
     * be delivered to manually entered recipients.
     */
    @ManyToOne
    @JoinColumn(name = "RecipientId", nullable = true)
    private Client recipient;

    /**
     * Full name of the shipment recipient.
     */
    @NotBlank(message = "Recipient name cannot be blank!")
    @Size(min = 2, max = 100,
            message = "Recipient name must be between 2 and 100 characters!")
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    /**
     * Contact phone number of the recipient.
     */
    @NotBlank(message = "Recipient phone cannot be blank!")
    @Size(min = 5, max = 20,
            message = "Recipient phone must be between 5 and 20 characters!")
    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    /**
     * Delivery address of the recipient.
     */
    @ManyToOne
    @JoinColumn(name = "RecipientAddress", nullable = true)
    private Address recipientAddress;

    /**
     * Type of shipment delivery.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "DeliveryType", nullable = false)
    private DeliveryType deliveryType;

    /**
     * Client who sends the shipment.
     */
    @ManyToOne
    @JoinColumn(name = "SenderId", nullable = false)
    private Client sender;

    /**
     * Address of the shipment sender.
     */
    @ManyToOne
    @JoinColumn(name = "SenderAddress", nullable = false)
    private Address senderAddress;

    /**
     * Office where the shipment is registered or delivered.
     */
    @ManyToOne
    @JoinColumn(name = "OfficeId", nullable = false)
    private Office office;

    /**
     * Weight of the shipment in kilograms.
     */
    @Positive(message = "The weight must be greater than 0")
    @Column(name = "Weight", nullable = false)
    private double weight;

    /**
     * Calculated shipment delivery price.
     */
    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    /**
     * Unique tracking identifier of the shipment.
     */
    @NotBlank(message = "Unique ID cannot be blank!")
    @Size(min = 10, max = 20,
            message = "Unique id has to be between 5 and 30 characters!")
    @Column(name = "uniqueId", nullable = false, unique = true)
    private String uniqueId;

    /**
     * Timestamp when the shipment was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last shipment update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}