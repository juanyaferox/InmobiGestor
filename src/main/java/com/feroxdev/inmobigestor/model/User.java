package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(nullable = false, length = 50)
    private String user;

    @Column(nullable = false)
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idBranch")
    private Branch branch;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Client> client;

}
