package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "historysale")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorySale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorySale;

    @ManyToOne
    @JoinColumn(name = "idEstate", nullable = false)
    private Estates estate;

    @ManyToOne
    @JoinColumn(name = "idClientPrevious", nullable = true)
    private Clients clientPrevious;

    @ManyToOne
    @JoinColumn(name = "idClientActual", nullable = true)
    private Clients clientActual;

    @Column(precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private LocalDate saleDate;
}
