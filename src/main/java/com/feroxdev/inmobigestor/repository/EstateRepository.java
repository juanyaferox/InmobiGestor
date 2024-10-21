package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.Estates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estates, Integer> {
}
