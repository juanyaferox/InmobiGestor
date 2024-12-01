package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @JoinColumn(name = "idEstate")
    private Estate estate;

    @ManyToOne
    @JoinColumn(name = "idClient")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "idClientRented")
    private Client clientRented;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate exitDate;

    private String rentPrice;
}