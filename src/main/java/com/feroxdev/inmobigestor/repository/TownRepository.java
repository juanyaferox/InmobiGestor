package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TownRepository extends JpaRepository<Town, Long> {

    @Query ("SELECT DISTINCT t FROM Town t WHERE t.idTown IN (SELECT b.town FROM Branch b) ORDER BY t.name ASC")
    List<Town> findAllTownsWithBranches();
}