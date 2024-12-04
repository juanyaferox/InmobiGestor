package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete (action = OnDeleteAction.SET_NULL)
    private Estate estate;

    @ManyToOne
    @JoinColumn(name = "idClientPrevious")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Client clientPrevious;

    @ManyToOne
    @JoinColumn(name = "idClientActual")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Client clientActual;

    @Column(precision = 10, scale = 2)
    private BigDecimal salePrice;

    private LocalDate saleDate;
}
