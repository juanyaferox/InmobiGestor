package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "branchs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branchs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBranch;

    @Column(nullable = false)
    private Integer idTown;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private Users user;
}
