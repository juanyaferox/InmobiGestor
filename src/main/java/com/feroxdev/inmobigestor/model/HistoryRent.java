package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historyrent")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryRent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistoryRent;

    @ManyToOne
    @JoinColumn(name = "idEstate", nullable = false)
    private Estates estate;

    @ManyToOne
    @JoinColumn(name = "idClient", nullable = false)
    private Clients client;

    @ManyToOne
    @JoinColumn(name = "idClientRented", nullable = false)
    private Clients clientRented;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;
    private LocalDate exitDate;
}