package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a logistics company.
 *
 * The entity stores basic company information such as
 * name, unique identification code, contact details, and address.
 */
@Getter
@Setter
@Entity
public class LogisticCompany {

    /**
     * Primary key of the logistics company.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Official name of the company.
     */
    @NotBlank(message = "Company name is required.")
    @Column(nullable = false)
    private String name;

    /**
     * Unique Identification Code (UIC/EIK) of the company.
     */
    @NotBlank(message = "UIC (EIK) is required.")
    @Column(nullable = false)
    private String uic;

    /**
     * Contact phone number of the company.
     */
    private String phone;

    /**
     * Contact email address of the company.
     */
    private String email;

    /**
     * Physical address of the company.
     */
    @Column(length = 1000)
    private String address;
}