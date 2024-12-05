package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Town;
import com.feroxdev.inmobigestor.model.User;
import com.feroxdev.inmobigestor.repository.BranchRepository;
import com.feroxdev.inmobigestor.repository.TownRepository;
import lombok.extern.slf4j.Slf4j;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchServiceImpl implements BranchService {
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private TownRepository townRepository;

    @Autowired
    private UserService userService;

    /**
     * Verifica si existe una sucursal en esa ciudad
     * @param town: ciudad a hacer comprobacion
     * @return La sucursal correspondiente o vacia si no existe
     */
    @Override
    public Branch verifyIfCityInBranch(Town town) {
        Optional<Branch> branchCoincident = branchRepository.findByTown(town);
        return branchCoincident.orElse(new Branch());
    }

    /**
     * Encuentra todas las sucursales
     * @return Lista de sucursales ordenadas por id
     */
    @Override
    public List<Branch> findAllBranch() {
        return branchRepository.findAll(Sort.by(Sort.Direction.ASC, "idBranch"));
    }

    /**
     *
     * @param branch
     * @return Branch mandada si el borrado es exitoso, branch vacia si el no se encontró en la bbdd
     * y null si está relacionada con algun usuario
     */
    @Override
    public Branch deleteBranch(Branch branch) {
        Integer id= branch.getIdBranch();
        Optional<Branch> optionalBranchs = branchRepository.findById(id);
        if (optionalBranchs.isPresent()){
            var branchGet = optionalBranchs.get();
            var listUser = userService.allUsersList();
            var existsUserWithThisBranch = false;
            for (int i = 1; i< listUser.size(); i++) {
                User user = listUser.get(i);
                if (branchGet.equals(user.getBranch())) {
                    existsUserWithThisBranch = true;
                    break;
                }
            }
            if (existsUserWithThisBranch)
                return null;

            branchRepository.delete(branchGet);
            return branchGet;
            //mirar si la sucursal esta relacionada a algun usuario
        }
        return new Branch();
    }

    /**
     * Añade una sucursal a la bbdd
     * @param branch sucursal a añadir
     * @return la sucursal añadida si la ciudad existe, null si no existe
     */
    @Override
    public Branch addBranch(Branch branch) {
        if (branch != null && townRepository.findById(branch.getTown().getIdTown()).isPresent()){
            return branchRepository.save(branch);
        }
        return null;
    }

    /**
     * Actualiza una sucursal
     * @param branch sucursal a actualizar
     * @return la sucursal actualizada si la ciudad existe y la sucursal también, null si no existe
     */
    @Override
    public Branch updateBranch(Branch branch) {
        if (branch != null && townRepository.findById(branch.getTown().getIdTown()).isPresent()){
            if (branchRepository.findById(branch.getIdBranch()).isPresent())
                return branchRepository.save(branch);
        }
        return null;
    }

}
