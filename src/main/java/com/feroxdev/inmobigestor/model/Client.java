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
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idClient;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idBranch")
    private Branch branch;

    private Integer idEstateRented;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String lastname1;

    @Column(length = 50)
    private String lastname2;

    private String email;

    @Column(length = 9)
    private String dni;

    @Column(length = 20)
    private String phone;

    private String address;

    private Integer type;
}
