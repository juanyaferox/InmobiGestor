package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<Town, Long> {
}