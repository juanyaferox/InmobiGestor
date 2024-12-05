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

    /**
     * Obtiene todos los historiales de venta de una sucursal
     * @param idBranch Sucursal
     * @return Lista de historiales de venta ordenados por id
     */
    @Override
    public List<HistorySale> getHistorySaleByBranch(Branch idBranch) {
            return hSaleRepository.findByBranchId(idBranch.getIdBranch())
                    .stream().sorted(
                            (h1, h2) -> h2.getIdHistorySale().compareTo(h1.getIdHistorySale())
                    ).toList();
    }

    /**
     * Guarda un historial de venta
     * @param historySale Historial de venta
     * @return Historial de venta guardado
     */
    @Override
    public HistorySale saveHistorySale(HistorySale historySale) {
        return hSaleRepository.save(historySale);
    }

    /**
     * Obtiene todos los historiales de venta de una sucursal y referencia
     * @param idBranch Sucursal
     * @param reference Referencia
     * @return Lista de historiales de venta ordenados por id
     */
    @Override
    public List<HistorySale> getHistorySaleByBranchAndReference(Branch idBranch, String reference) {
        return hSaleRepository.findByBranchIdAndReference(idBranch.getIdBranch(), reference)
                .stream().sorted(
                        (h1, h2) -> h2.getIdHistorySale().compareTo(h1.getIdHistorySale())
                ).toList();
    }
}
