package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.enums.EnumClient;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Iterable<Client> findAllByBranch(Branch branch);
    Iterable<Client> findAllByBranchAndType(Branch branch, EnumClient type);

}
