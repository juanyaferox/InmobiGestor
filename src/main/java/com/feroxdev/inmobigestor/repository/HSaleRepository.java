package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.HistorySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HSaleRepository extends JpaRepository<HistorySale, Integer> {
}
