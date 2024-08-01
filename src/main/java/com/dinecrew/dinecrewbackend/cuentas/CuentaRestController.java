package com.dinecrew.dinecrewbackend.cuentas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaRestController {

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<?> guardarCuenta(
            @RequestBody CuentaDto dto
    ) {
        // Se comprueba que el pedido tiene al menos un producto
        if (dto.getItems().isEmpty()) {
            return ResponseEntity.status(400).body("Debe haber al menos un producto en la cuenta");
        }

        Cuenta cuenta = Cuenta.builder()
                .total(dto.getTotal())
                .items(dto.getItems())
                .timestamp(Instant.now())
                .build();

        cuentaService.crearCuenta(cuenta);

        return ResponseEntity.ok("Cuenta " + cuenta + " guardada correctamente");
    }
}
