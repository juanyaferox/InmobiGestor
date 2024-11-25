package com.feroxdev.inmobigestor.model;

import com.feroxdev.inmobigestor.enums.EnumEstate;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "estates")
@Data
@ToString
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
}
