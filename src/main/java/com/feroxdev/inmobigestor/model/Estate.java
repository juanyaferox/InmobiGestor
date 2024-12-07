package com.feroxdev.inmobigestor.model;

import com.feroxdev.inmobigestor.enums.EnumClient;
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

    @OneToOne(mappedBy = "estateRented", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Client clientRenter;

    @OneToMany(mappedBy = "estate", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistoryRent> historyRents;

    @OneToMany(mappedBy = "estate", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistorySale> historySales;

    @PreRemove
    private void preRemove() {
        if (clientRenter != null) {
            clientRenter.setEstateRented(null);
            if (clientRenter.getType().equals(EnumClient.RENTER_AND_HOUSE_OWNER)) {
                clientRenter.setType(EnumClient.HOUSE_OWNER);
            } else {
                clientRenter.setType(EnumClient.INACTIVE);
            }
        }
    }
}
