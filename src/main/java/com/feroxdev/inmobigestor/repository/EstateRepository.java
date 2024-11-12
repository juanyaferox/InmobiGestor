package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Integer> {
}
