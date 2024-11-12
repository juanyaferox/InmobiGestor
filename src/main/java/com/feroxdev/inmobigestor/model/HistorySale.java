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
    @JoinColumn(name = "idEstate")
    private Estate estate;

    @ManyToOne
    @JoinColumn(name = "idClientPrevious")
    private Client clientPrevious;

    @ManyToOne
    @JoinColumn(name = "idClientActual")
    private Client clientActual;

    @Column(precision = 10, scale = 2)
    private BigDecimal salePrice;

    private LocalDate saleDate;
}
