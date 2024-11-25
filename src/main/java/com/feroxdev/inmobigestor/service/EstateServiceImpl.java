package com.feroxdev.inmobigestor.service;

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
    public Iterable<Estate> getEstatesByState(int state) {
        return estateRepository.findAllByState(state);
    }
}
