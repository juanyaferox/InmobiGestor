package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "estates")
@Data
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

    @ManyToOne
    @JoinColumn(name = "idBranch")
    private Branch branch;

    private Integer reference;

    @Column(length = 500)
    private String fullAddress;

    @Column(length = 500)
    private String imagePath;

    private Integer state;
}
