package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.HistoryRent;
import com.feroxdev.inmobigestor.repository.BranchRepository;
import com.feroxdev.inmobigestor.repository.HRentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class HRentServiceImpl implements HRentService{

    @Autowired
    private HRentRepository hRentRepository;

    @Override
    public List<HistoryRent> getHistoryRentByBranch(Branch branch) {
        return hRentRepository.findByBranchId(branch.getIdBranch());
    }

    @Override
    public HistoryRent saveHistoryRent(HistoryRent historyRent) {
        return hRentRepository.save(historyRent);
    }
}
