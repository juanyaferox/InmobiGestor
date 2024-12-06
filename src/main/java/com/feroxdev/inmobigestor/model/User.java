package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    private String user;

    private String password;

    private String email;

    private String name;

    private String lastname1;

    private String lastname2;

    private String dni;

    @ManyToOne
    @JoinColumn(name = "idBranch")
    private Branch branch;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Client> client;

    @PreRemove
    private void preRemove() {
        for (Client client : client) {
            client.setUser(null);
        }
    }

}
