package com.dinecrew.dinecrewbackend.mesas;

import com.dinecrew.dinecrewbackend.config.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mesas")
public class MesaRestController {

    @Autowired
    private MesaService mesaService;

    @GetMapping(value = {"/", ""})
    public ResponseEntity<?> getMesas() {
        List<Mesa> mesas = mesaService.findAll();
        return ResponseEntity.ok(MesaDto.fromDocuments(mesas));
    }

    @PostMapping(value = {"/{numMesas}"})
    public ResponseEntity<?> inicializarMesas(
            @PathVariable int numMesas
    ) {
        Map<String, String> response = new HashMap<>();

        for (int i = 0; i < numMesas; i++) {
            mesaService.save(Mesa.builder()
                    .numero(String.valueOf(i + 1))
                    .userId(null)
                    .build());
        }
        response.put("message", "Mesas inicializadas correctamente");
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> agregarMesa() {

        Map<String, String> response = new HashMap<>();
        mesaService.save(Mesa.builder()
                .numero(String.valueOf(mesaService.findAll().size() + 1))
                .userId(null)
                .build());
        response.put("message", "Mesa agregada correctamente");
        return ResponseEntity.status(201).body(response);
    }

}
