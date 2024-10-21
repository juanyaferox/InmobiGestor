package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Integer> {
}
