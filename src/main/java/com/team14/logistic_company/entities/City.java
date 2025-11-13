package com.team14.logistic_company.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
public class City {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    @NotBlank(message = "The city name cannot be blank!")
    @Size(min = 3, max = 20, message = "The city name has to be between 3 and 20 characters!")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Getter
    @OneToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}