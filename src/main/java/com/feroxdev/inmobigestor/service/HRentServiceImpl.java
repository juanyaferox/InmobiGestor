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

    /**
     * Obtiene todos los historiales de renta de una sucursal
     * @param branch Sucursal
     * @return Lista de historiales de renta ordenados por id
     */
    @Override
    public List<HistoryRent> getHistoryRentByBranch(Branch branch) {

        return hRentRepository.findByBranchId(branch.getIdBranch())
                .stream().sorted(
                        (h1, h2) -> h2.getIdHistoryRent().compareTo(h1.getIdHistoryRent())
                ).toList();
    }

    /**
     * Guarda un historial de renta
     * @param historyRent Historial de renta
     * @return Historial de renta guardado
     */
    @Override
    public HistoryRent saveHistoryRent(HistoryRent historyRent) {
        return hRentRepository.save(historyRent);
    }

    /**
     * Obtiene todos los historiales de renta de una sucursal y referencia
     * @param idBranch Sucursal
     * @param reference Referencia
     * @return Lista de historiales de renta ordenados por id
     */
    @Override
    public List<HistoryRent> getHistoryRentByBranchAndReference(Branch idBranch, String reference) {
        return hRentRepository.findByBranchIdAndReference(idBranch.getIdBranch(), reference)
                .stream().sorted(
                (h1, h2) -> h2.getIdHistoryRent().compareTo(h1.getIdHistoryRent())
        ).toList();
    }
}
