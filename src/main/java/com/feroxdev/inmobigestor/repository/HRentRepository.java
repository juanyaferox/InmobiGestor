package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.HistoryRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HRentRepository extends JpaRepository<HistoryRent, Integer> {

    @Query ("SELECT hr FROM HistoryRent hr WHERE hr.estate.branch.idBranch = :idBranch")
    List<HistoryRent> findByBranchId(@Param ("idBranch") Integer idBranch);

    @Query ("SELECT hr FROM HistoryRent hr WHERE hr.estate.branch.idBranch = :idBranch AND hr.estate.reference LIKE CONCAT('%', :reference, '%')")
    List<HistoryRent> findByBranchIdAndReference(@Param ("idBranch") Integer idBranch, @Param ("reference") String reference);
}
