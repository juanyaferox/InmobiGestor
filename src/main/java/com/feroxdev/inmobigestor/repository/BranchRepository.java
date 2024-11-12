package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    Optional<Branch> findByTown(Town town);
}
