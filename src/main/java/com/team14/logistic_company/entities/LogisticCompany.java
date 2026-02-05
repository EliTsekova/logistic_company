package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LogisticCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Company name is required.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "UIC (EIK) is required.")
    @Column(nullable = false)
    private String uic; // ЕИК

    private String phone;
    private String email;

    @OneToOne
    @JoinColumn(name = "AddressId")
    private Address address;
}
