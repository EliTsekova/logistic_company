package com.team14.logistic_company.entities;

import com.team14.logistic_company.entities.enums.PositionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing an employee in the logistics company system.
 *
 * Employees can work as office employees or couriers
 * and may be assigned to a specific office.
 */
@Entity
@Getter
@SuppressWarnings("unused")
public class Employee {

    /**
     * Primary key of the employee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * User account associated with the employee.
     */
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Office assigned to the employee.
     * Couriers may not have a fixed office.
     */
    @Setter
    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

    /**
     * Position type of the employee.
     */
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "position_type", nullable = false)
    private PositionType positionType;

    /**
     * Timestamp when the employee record was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last employee update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}