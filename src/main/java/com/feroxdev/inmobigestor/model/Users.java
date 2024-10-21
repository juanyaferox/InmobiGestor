package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(nullable = false, length = 50)
    private String user;

    @Column(nullable = false, length = 255)
    private String password;

    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String lastname1;

    @Column(nullable = false, length = 50)
    private String lastname2;

    @Column(nullable = false, length = 9)
    private String dni;

    @ManyToOne
    @JoinColumn(name = "idBranch", nullable = true)
    private Branchs branch;
}
