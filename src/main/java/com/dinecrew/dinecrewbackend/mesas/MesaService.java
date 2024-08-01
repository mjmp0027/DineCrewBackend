package com.dinecrew.dinecrewbackend.mesas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    @Autowired
    private MesaRepository repository;


    public List<Mesa> findAll() {
        return repository.findAll();
    }
    public List<Mesa> findAllByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }

    public Mesa findByNumero(String numero) {
        return repository.findByNumero(numero).orElse(null);
    }

    public void saveAll(List<Mesa> mesas) {
        repository.saveAll(mesas);
    }

    public Mesa save(Mesa mesa) {
        return repository.save(mesa);
    }
}
