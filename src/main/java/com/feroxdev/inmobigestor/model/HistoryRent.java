package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
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
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Estate estate;

    @ManyToOne
    @JoinColumn(name = "idClient")
    @OnDelete (action = OnDeleteAction.SET_NULL)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "idClientRented")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Client clientRented;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate exitDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal rentPrice;
}