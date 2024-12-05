package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.enums.EnumEstate;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Integer> {
    /**
     * Encuentra todas las propiedades por estado
     * @param state Estado
     * @return Lista de propiedades
     */
    Iterable<Estate> findAllByState(EnumEstate state);

    Iterable<Estate> findAllByBranch(Branch branch);

    Iterable<Estate> findAllByStateAndBranch(EnumEstate state, Branch branch);
}
