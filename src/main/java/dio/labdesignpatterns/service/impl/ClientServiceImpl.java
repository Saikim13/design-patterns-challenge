package dio.labdesignpatterns.service.impl;

import dio.labdesignpatterns.model.Address;
import dio.labdesignpatterns.model.Client;
import dio.labdesignpatterns.repository.AddressRepository;
import dio.labdesignpatterns.repository.ClientRepository;
import dio.labdesignpatterns.service.AddressService;
import dio.labdesignpatterns.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;
    @Override
    public Iterable<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.get();
    }

    @Override
    public void insert(Client client) {
        saveClientWithAddress(client);
    }

    @Override
    public void update(Long id, Client client) {
        Optional<Client> clientDb = clientRepository.findById(id);
        if (clientDb.isPresent()) {
            saveClientWithAddress(client);
        }
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    private void saveClientWithAddress(Client client) {
        String postalCode = client.getAddress().getCep();
        Address address = addressRepository.findById(postalCode).orElseGet(() -> {
            Address newAddress = addressService.getPostalCode(postalCode);
            addressRepository.save(newAddress);
            return newAddress;
        });
        client.setAddress(address);
        clientRepository.save(client);
    }

}
