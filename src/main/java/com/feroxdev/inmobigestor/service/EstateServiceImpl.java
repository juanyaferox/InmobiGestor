package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.enums.EnumEstate;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Estate;
import com.feroxdev.inmobigestor.repository.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstateServiceImpl implements EstateService {

    @Autowired
    private EstateRepository estateRepository;

    @Override
    public Iterable<Estate> getAllEstates() {
        return estateRepository.findAll();
    }

    @Override
    public Iterable<Estate> getEstatesByBranch(Branch branch) {
        return estateRepository.findAllByBranch(branch);
    }

    @Override
    public Iterable<Estate> getEstatesByState(EnumEstate state) {
        return estateRepository.findAllByState(state);
    }

    @Override
    public Iterable<Estate> getEstatesByStateAndBranch(EnumEstate state, Branch branch) {
        return estateRepository.findAllByStateAndBranch(state, branch);
    }
}
