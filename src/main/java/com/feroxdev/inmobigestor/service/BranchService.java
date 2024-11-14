package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Town;

import java.util.List;

public interface BranchService {

    Branch verifyIfCityInBranch(Town town);
    List<Branch> findAllBranch();
    Branch deleteBranch(Branch branch);
    Branch addBranch(Branch branch);
    Branch updateBranch(Branch branch);
}
