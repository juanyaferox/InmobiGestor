package com.feroxdev.inmobigestor.model;

import com.feroxdev.inmobigestor.enums.EnumClient;
import com.feroxdev.inmobigestor.enums.EnumEstate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clients")
@Data
@ToString(exclude = "estates")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idBranch")
    private Branch branch;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEstateRented")
    private Estate estateRented;

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

    @Enumerated(EnumType.ORDINAL)
    private EnumClient type;

    @OneToMany (mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Estate> estates;

    public String getFullName() {
        return name + " " + lastname1 + " " + (lastname2 != null ? lastname2 : "");
    }
}
