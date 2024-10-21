package com.feroxdev.inmobigestor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idClient;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = true)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "idBranch", nullable = false)
    private Branchs branch;

    private Integer idEstateRented;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String lastname1;

    private String lastname2;
    private String email;

    @Column(nullable = false, length = 9)
    private String dni;

    private String phone;
    private String address;
    private Integer type;
}
