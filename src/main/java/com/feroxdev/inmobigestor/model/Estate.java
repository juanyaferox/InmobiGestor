package com.feroxdev.inmobigestor.model;

import com.feroxdev.inmobigestor.enums.EnumEstate;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "estates")
@Data
@ToString(exclude = { "clientRenter","historyRents", "historySales" })
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstate;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idBranch")
    private Branch branch;

    private String reference;

    @Column(length = 500)
    private String fullAddress;

    @Column(length = 500)
    private String imagePath;

    @Enumerated(EnumType.ORDINAL)
    private EnumEstate state;

    @OneToOne(mappedBy = "estateRented", fetch = FetchType.EAGER)
    private Client clientRenter;

    @OneToMany(mappedBy = "estate", fetch = FetchType.EAGER)
    private List<HistoryRent> historyRents;

    @OneToMany(mappedBy = "estate", fetch = FetchType.EAGER)
    private List<HistorySale> historySales;
}
