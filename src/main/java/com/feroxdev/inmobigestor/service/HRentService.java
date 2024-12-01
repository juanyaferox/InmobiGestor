package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.HistoryRent;

import java.util.List;

public interface HRentService {
    List<HistoryRent> getHistoryRentByBranch(Branch idBranch);
    HistoryRent saveHistoryRent(HistoryRent historyRent);
    List<HistoryRent> getHistoryRentByBranchAndReference(Branch idBranch, String reference);
}
