package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.enums.EnumClient;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    /**
     * Encuentra todos los clientes por sucursal
     * @param branch Sucursal
     * @return Lista de clientes
     */
    Iterable<Client> findAllByBranch(Branch branch);

    /**
     * Encuentra todos los clientes por sucursal y tipo de cliente
     * @param branch Sucursal
     * @param type Tipo de cliente
     * @return Lista de clientes
     */
    Iterable<Client> findAllByBranchAndType(Branch branch, EnumClient type);

}
