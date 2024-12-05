package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.enums.EnumEstate;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Estate;
import com.feroxdev.inmobigestor.repository.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstateServiceImpl implements EstateService {

    @Autowired
    private EstateRepository estateRepository;

    /**
     * Obtiene todas las propiedades
     * @return Lista de propiedades
     */
    @Override
    public Iterable<Estate> getAllEstates() {
        return estateRepository.findAll();
    }

    /**
     * Obtiene todas las propiedades por sucursal
     * @param branch Sucursal
     * @return Lista de propiedades
     */
    @Override
    public Iterable<Estate> getEstatesByBranch(Branch branch) {
        return estateRepository.findAllByBranch(branch);
    }

    /**
     * Obtiene todas las propiedades por estado
     * @param state Estado
     * @return Lista de propiedades
     */
    @Override
    public Iterable<Estate> getEstatesByState(EnumEstate state) {
        return estateRepository.findAllByState(state);
    }

    /**
     * Obtiene todas las propiedades por estado y sucursal
     * @param state Estado
     * @param branch Sucursal
     * @return Lista de propiedades
     */
    @Override
    public Iterable<Estate> getEstatesByStateAndBranch(EnumEstate state, Branch branch) {
        return estateRepository.findAllByStateAndBranch(state, branch);
    }

    /**
     * Borra una propiedad
     * @param estate Id de la propiedad
     * @return Propiedad
     */
    @Override
    public Estate deleteEstate(Estate estate) {
        estateRepository.delete(estate);
        return estate;
    }

    /**
     * Guarda una propiedad
     * @param estate Propiedad
     * @return Propiedad guardada
     */
    @Override
    public Estate saveEstate(Estate estate) {
        return estateRepository.save(estate);
    }

    /**
     * Actualiza una propiedad
     * @param estate Propiedad
     * @return Propiedad actualizada
     */
    @Override
    public Estate updateEstate(Estate estate) {
        return estateRepository.save(estate);
    }
}
