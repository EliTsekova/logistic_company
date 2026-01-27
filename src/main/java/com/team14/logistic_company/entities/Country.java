package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Getter
public class Country {
    @Getter  // ДОБАВЕНО
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    @NotBlank(message = "The name of the country cannot be blank!")
    @Size(min = 3, max = 50, message = "The name of the country has to be between 3 and 50 characters!")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Getter
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    @Getter
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}