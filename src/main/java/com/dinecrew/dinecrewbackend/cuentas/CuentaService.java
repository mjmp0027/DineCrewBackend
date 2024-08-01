package com.dinecrew.dinecrewbackend.cuentas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository repository;

    public void crearCuenta(Cuenta cuenta) {
        repository.save(cuenta);
    }
}
