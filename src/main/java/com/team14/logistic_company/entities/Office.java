package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
public class Office {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @NotBlank(message = "Title cannot be blank!")
    @Size(min = 5, max = 20, message = "Title has to be between 5 and 20 characters!")
    @Column(name = "Title", nullable = false)
    private String title;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}
