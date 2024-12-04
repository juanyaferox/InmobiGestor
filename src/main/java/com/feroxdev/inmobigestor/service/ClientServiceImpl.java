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
        if (!client.getEstates().isEmpty()){
            client.setType(EnumClient.RENTER_AND_HOUSE_OWNER);
            return saveClient(client);
        }
        client.setType(EnumClient.RENTER);
        return saveClient(client);
    }

    @Override
    public Client saveClientAsNoRenter(Client client) {
        if (!client.getEstates().isEmpty()){
            client.setType(EnumClient.HOUSE_OWNER);
            return saveClient(client);
        }
        client.setType(EnumClient.INACTIVE);
        return saveClient(client);
    }

    @Override
    public Client saveClientAsOwner(Client client) {
        if (client.getType() == EnumClient.RENTER){
            client.setType(EnumClient.RENTER_AND_HOUSE_OWNER);
            return saveClient(client);
        }
//        client.getType() == EnumClient.ANOTHER ||
        else if (client.getType() == EnumClient.INACTIVE) {
            client.setType(EnumClient.HOUSE_OWNER);
            return saveClient(client);
        }
        return client;
    }

    @Override
    public Client saveClientAsNoOwner(Client client) {

        if (client.getType() == EnumClient.RENTER_AND_HOUSE_OWNER){
            client.setType(EnumClient.RENTER);
            return saveClient(client);
        }
        else if (client.getType() == EnumClient.HOUSE_OWNER){
            client.setType(EnumClient.INACTIVE);
            return saveClient(client);
        }
        return client;
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
