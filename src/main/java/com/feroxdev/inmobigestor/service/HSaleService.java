package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.HistorySale;

import java.util.List;

public interface HSaleService {
    List<HistorySale> getHistorySaleByBranch(Branch idBranch);
    HistorySale saveHistorySale(HistorySale historySale);
    List<HistorySale> getHistorySaleByBranchAndReference(Branch idBranch, String reference);
}
