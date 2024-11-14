package com.feroxdev.inmobigestor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "towns")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Town {

    @Id
    Integer idTown;

    @Column
    String name;
}
