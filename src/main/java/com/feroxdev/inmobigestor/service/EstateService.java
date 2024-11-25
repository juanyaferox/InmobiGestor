package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Estate;

public interface EstateService {

    Iterable<Estate> getAllEstates();
    Iterable<Estate> getEstatesByState(int state);
}
