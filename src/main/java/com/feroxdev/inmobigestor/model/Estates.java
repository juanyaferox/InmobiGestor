package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "estates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstate;

    @ManyToOne
    @JoinColumn(name = "idClient", nullable = true)
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "idBranch", nullable = false)
    private Branchs branch;

    @Column(nullable = true)
    private Integer reference;

    @Column(nullable = true, length = 500)
    private String fullAddress;

    @Column(nullable = true, length = 500)
    private String imagePath;

    @Column(nullable = true)
    private Integer state;
}
