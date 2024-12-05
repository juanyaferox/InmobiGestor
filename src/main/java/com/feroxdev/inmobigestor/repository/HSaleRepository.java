package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.HistoryRent;
import com.feroxdev.inmobigestor.model.HistorySale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HSaleRepository extends JpaRepository<HistorySale, Integer> {

    /**
     * Encuentra todos los historiales de venta por sucursal
     * @param idBranch Id de la sucursal
     * @return Lista de historiales de venta
     */
    @Query ("SELECT hs FROM HistorySale hs WHERE hs.estate.branch.idBranch = :idBranch")
    List<HistorySale> findByBranchId(@Param ("idBranch") Integer idBranch);

    /**
     * Encuentra todos los historiales de venta por sucursal y referencia
     * @param idBranch Id de la sucursal
     * @param reference Referencia
     * @return Lista de historiales de venta
     */
    @Query ("SELECT hs FROM HistorySale hs WHERE hs.estate.branch.idBranch = :idBranch AND hs.estate.reference LIKE CONCAT('%', :reference, '%')")
    List<HistorySale> findByBranchIdAndReference(@Param ("idBranch") Integer idBranch, @Param ("reference") String reference);
}
