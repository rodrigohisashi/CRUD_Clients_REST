package com.example.clientsRHT.Rodrigo.services;

import com.example.clientsRHT.Rodrigo.dto.ClientDTO;
import com.example.clientsRHT.Rodrigo.entities.Client;
import com.example.clientsRHT.Rodrigo.exceptions.DataBaseException;
import com.example.clientsRHT.Rodrigo.exceptions.ResourceNotFoundException;
import com.example.clientsRHT.Rodrigo.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
        Page<Client> list = repository.findAll(pageRequest);

        return list.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(long id) {
        Optional<Client> client = repository.findById(id);
        Client entity = client.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada!"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        Client entity = new Client();
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(long id, ClientDTO clientDTO) {
        try {
            Client entity = repository.getById(id);
            dtoToEntity(clientDTO, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        } catch(EntityNotFoundException e) {
            throw new ResourceNotFoundException("O id " + id + " não foi encontrado." );
        }
    }

    public void delete(long id) {
        try {
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("O id " + id + " não foi encontrado." );

        } catch(DataIntegrityViolationException e) {
            throw new DataBaseException("Violação de integridade no banco de dados");
        }
    }




    private void dtoToEntity(ClientDTO dto, Client entity) {
     entity.setName(dto.getName());
     entity.setCpf(dto.getCpf());
     entity.setIncome(dto.getIncome());
     entity.setBirthDate(dto.getBirthDate());
     entity.setChildren(dto.getChildren());
    }



}
