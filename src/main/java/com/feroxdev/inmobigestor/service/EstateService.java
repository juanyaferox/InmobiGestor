package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.enums.EnumEstate;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Estate;

public interface EstateService {

    Iterable<Estate> getAllEstates();
    Iterable<Estate> getEstatesByState(EnumEstate state);

    Iterable<Estate> getEstatesByBranch(Branch branch);
    Iterable<Estate> getEstatesByStateAndBranch(EnumEstate state, Branch branch);

    Estate deleteEstate(Estate estate);
}
