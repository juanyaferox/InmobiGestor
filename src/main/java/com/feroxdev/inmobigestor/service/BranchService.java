package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Town;

public interface BranchService {

    Branch verifyIfCityInBranch(Town town);
}
