package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "branchs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBranch;

    @ManyToOne
    @JoinColumn(name = "idTown")
    private Town town;
}
