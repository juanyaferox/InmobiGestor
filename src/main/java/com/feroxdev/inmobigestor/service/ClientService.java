package com.feroxdev.inmobigestor.service;

import com.feroxdev.inmobigestor.enums.EnumClient;
import com.feroxdev.inmobigestor.model.Branch;
import com.feroxdev.inmobigestor.model.Client;
import com.feroxdev.inmobigestor.model.Estate;

public interface ClientService {

    Iterable<Client> getAllClients();
    Iterable<Client> getAllClientsByBranch(Branch branch);

    Iterable<Client> getAllClientsByBranchAndType(Branch branch, EnumClient type);

    Client getClientById(Integer idClient);

    Client saveClientAsRenter(Client client);

    Client saveClientAsNoRenter(Client client);

    Client saveClientAsOwner(Client client);

    Client saveClientAsNoOwner(Client client);

    Client saveClient(Client client);

    Client deleteClient(Client client);
}
