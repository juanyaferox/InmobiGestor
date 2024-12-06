package com.feroxdev.inmobigestor.model;

import com.feroxdev.inmobigestor.enums.EnumClient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clients")
@Data
@ToString
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

    private String name;

    private String lastname1;

    private String lastname2;

    private String email;

    private String dni;

    private String phone;

    private String address;

    @Enumerated(EnumType.ORDINAL)
    private EnumClient type;

    @ToString.Exclude
    @OneToMany (mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Estate> estates;

    @ToString.Exclude
    @OneToMany (mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistoryRent> historyRentsPrevious;

    @ToString.Exclude
    @OneToMany (mappedBy = "clientRented", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistoryRent> historyRentsActual;

    @ToString.Exclude
    @OneToMany (mappedBy = "clientPrevious", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistorySale> historySalesPrevious;

    @ToString.Exclude
    @OneToMany (mappedBy = "clientActual", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistorySale> historySalesActual;

    public String getFullName() {
        return name + " " + lastname1 + " " + (lastname2 != null ? lastname2 : "");
    }
}
