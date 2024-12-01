package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.HistoryRent;
import com.feroxdev.inmobigestor.model.HistorySale;
import com.feroxdev.inmobigestor.repository.HSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HSaleServiceImpl implements HSaleService {

    @Autowired
    private HSaleRepository hSaleRepository;

    @Override
    public List<HistorySale> getHistorySaleByBranch(Branch idBranch) {
            return hSaleRepository.findByBranchId(idBranch.getIdBranch())
                    .stream().sorted(
                            (h1, h2) -> h2.getIdHistorySale().compareTo(h1.getIdHistorySale())
                    ).toList();
    }

    @Override
    public HistorySale saveHistorySale(HistorySale historySale) {
        return hSaleRepository.save(historySale);
    }

    @Override
    public List<HistorySale> getHistorySaleByBranchAndReference(Branch idBranch, String reference) {
        return hSaleRepository.findByBranchIdAndReference(idBranch.getIdBranch(), reference)
                .stream().sorted(
                        (h1, h2) -> h2.getIdHistorySale().compareTo(h1.getIdHistorySale())
                ).toList();
    }
}
