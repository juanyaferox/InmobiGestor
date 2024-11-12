package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Town;
import com.feroxdev.inmobigestor.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BranchServiceImpl implements BranchService {
    @Autowired
    private BranchRepository branchRepository;

    /**
     * Verifica si existe una sucursal en esa ciudad
     * @param town: ciudad a hacer comprobacion
     * @return La sucursal correspondiente o vacia si no existe
     */
    @Override
    public Branch verifyIfCityInBranch(Town town) {
        Optional<Branch> branchCoincident = branchRepository.findByTown(town);
        return branchCoincident.orElse(new Branch());
    }
}
