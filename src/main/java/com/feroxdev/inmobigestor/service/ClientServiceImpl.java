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

    /**
     * Obtiene todos los clientes
     * @return Lista de clientes
     */
    @Override
    public Iterable<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Obtiene todos los clientes por sucursal
     * @param branch Sucursal
     * @return Lista de clientes
     */
    @Override
    public Iterable<Client> getAllClientsByBranch(Branch branch) {
        return clientRepository.findAllByBranch(branch);
    }

    /**
     * Obtiene todos los clientes por sucursal y tipo de cliente
     * @param branch Sucursal
     * @param type Tipo de cliente
     * @return Lista de clientes
     */
    @Override
    public Iterable<Client> getAllClientsByBranchAndType(Branch branch, EnumClient type) {
        return clientRepository.findAllByBranchAndType(branch, type);
    }

    /**
     * Obtiene un cliente por id
     * @param idClient Id del cliente
     * @return Cliente
     */
    @Override
    public Client getClientById(Integer idClient) {
        return clientRepository.findById(idClient).orElse(null);
    }

    /**
     * Guarda un cliente como arrendatario
     * @param client Cliente
     * @return Cliente guardado que puede ser arrendatario o arrendatario y dueño
     */
    @Override
    public Client saveClientAsRenter(Client client) {
        if (!client.getEstates().isEmpty()){
            client.setType(EnumClient.RENTER_AND_HOUSE_OWNER);
            return saveClient(client);
        }
        client.setType(EnumClient.RENTER);
        return saveClient(client);
    }

    /**
     * Guarda un cliente como no arrendatario
     * @param client Cliente
     * @return Cliente guardado que puede ser dueño o inactivo
     */
    @Override
    public Client saveClientAsNoRenter(Client client) {
        if (!client.getEstates().isEmpty()){
            client.setType(EnumClient.HOUSE_OWNER);
            return saveClient(client);
        }
        client.setType(EnumClient.INACTIVE);
        return saveClient(client);
    }

    /**
     * Guarda un cliente como dueño
     * @param client Cliente
     * @return Cliente guardado que puede ser dueño o arrendatario y dueño
     */
    @Override
    public Client saveClientAsOwner(Client client) {
        if (client.getType() == EnumClient.RENTER){
            client.setType(EnumClient.RENTER_AND_HOUSE_OWNER);
            return saveClient(client);
        }
//        client.getType() == EnumClient.ANOTHER ||
        else if (client.getType() == EnumClient.INACTIVE || client.getType() == null) {
            client.setType(EnumClient.HOUSE_OWNER);
            return saveClient(client);
        }
        return client;
    }

    /**
     * Guarda un cliente como no dueño
     * @param client Cliente
     * @return Cliente guardado que puede ser arrendatario o inactivo
     */
    @Override
    public Client saveClientAsNoOwner(Client client) {

        if (client.getType() == EnumClient.RENTER_AND_HOUSE_OWNER){
            client.setType(EnumClient.RENTER);
            return saveClient(client);
        }
        else if (client.getType() == EnumClient.HOUSE_OWNER || client.getType() == null){
            client.setType(EnumClient.INACTIVE);
            return saveClient(client);
        }
        return client;
    }

    /**
     * Guarda un cliente
     * @param client Cliente
     * @return Cliente guardado
     */
    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Elimina un cliente
     * @param client Cliente
     * @return Cliente eliminado
     */
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
