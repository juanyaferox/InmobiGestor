package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.HistoryRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HRentRepository extends JpaRepository<HistoryRent, Integer> {
}
