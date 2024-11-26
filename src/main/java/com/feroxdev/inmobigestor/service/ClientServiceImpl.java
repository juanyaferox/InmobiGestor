package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.enums.EnumClient;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Client;
import com.feroxdev.inmobigestor.model.Estate;
import com.feroxdev.inmobigestor.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Iterable<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Iterable<Client> getAllClientsByBranch(Branch branch) {
        return clientRepository.findAllByBranch(branch);
    }

    @Override
    public Iterable<Client> getAllClientsByBranchAndType(Branch branch, EnumClient type) {
        return clientRepository.findAllByBranchAndType(branch, type);
    }
}
