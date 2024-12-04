package com.feroxdev.inmobigestor.model;

import lombok.*;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "branchs")
@Data
@ToString(exclude = { "Estates", "Users" })
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBranch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idTown")
    private Town town;

    String reference;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Estate> Estates;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<User> Users;
}
