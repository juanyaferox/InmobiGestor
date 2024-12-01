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

    @Override
    public Client getClientById(Integer idClient) {
        return clientRepository.findById(idClient).orElse(null);
    }

    @Override
    public Client saveClientAsRenter(Client client) {
        var clientToSave = getClientById(client.getIdClient());
        if (!client.getEstates().isEmpty()){
            clientToSave.setType(EnumClient.RENTER_AND_HOUSE_OWNER);
            return saveClient(clientToSave);
        }
        clientToSave.setType(EnumClient.RENTER);
        return saveClient(clientToSave);
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client deleteClient(Client client) {
        if (clientRepository.findById(client.getIdClient()).isEmpty())
            return null;
        if (client.getEstates()!=null && !client.getEstates().isEmpty() && client.getEstateRented()!=null)
            return null;
        clientRepository.delete(client);
        return client;
    }
}
