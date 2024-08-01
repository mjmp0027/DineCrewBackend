package com.dinecrew.dinecrewbackend.pedidos;

import com.dinecrew.dinecrewbackend.enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedido(
            @PathVariable String id
    ) {
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPedido(id);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El pedido con el id: " + id + " no se ha encontrado");
        }

        Pedido pedido = pedidoOpt.get();

        return ResponseEntity.status(200).body(PedidoDtoOut.fromDocument(pedido));
    }

    @GetMapping
    public ResponseEntity<?> obtenerPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerPedidos();
        return ResponseEntity.ok(PedidoDtoOut.fromDocuments(pedidos));
    }

    @GetMapping("/{mesa}/{estado}")
    public ResponseEntity<?> obtenerPedidos(
            @PathVariable String mesa,
            @PathVariable State estado
    ) {
        List<Pedido> pedidos = pedidoService.obtenerPedidosPorMesaAndEstado(mesa, estado);
        return ResponseEntity.ok(PedidoDtoOut.fromDocuments(pedidos));
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestBody PedidoDtoIn dto
    ) {
        // Se comprueba que viene la mesa
        if (dto.getMesa() == null || dto.getMesa().isEmpty()) {
            return ResponseEntity.status(400).body("Debe indicar la mesa del pedido");
        }

        // Se comprueba que el pedido tiene al menos un producto
        if (dto.getItems().isEmpty()) {
            return ResponseEntity.status(400).body("Debe haber al menos un producto en el pedido");
        }

        // Se crea el pedido con estado PENDIENTE y se guarda en la base de datos
        Pedido pedido = Pedido.builder()
                .estado(State.PENDIENTE)
                .items(dto.getItems())
                .mesa(dto.getMesa())
                .timestamp(Instant.now())
                .build();

        pedido = pedidoService.crearPedido(pedido);

        return ResponseEntity.status(201).body(PedidoDtoOut.fromDocument(pedido));
    }

    // Endpoint para actualizar los productos de un pedido
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(
            @PathVariable String id,
            @RequestBody PedidoDtoIn dto
    ) {
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPedido(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El pedido con el id: " + id + " no se ha encontrado");
        }

        Pedido pedido = pedidoOpt.get();

        if (dto.getMesa() == null || dto.getMesa().isEmpty()) {
            return ResponseEntity.status(400).body("Debe indicar la mesa del pedido");
        }

        if (dto.getItems().isEmpty()) {
            return ResponseEntity.status(400).body("Debe haber al menos un producto en el pedido");
        }

        pedido.setMesa(dto.getMesa());
        pedido.setItems(dto.getItems());

        pedido = pedidoService.crearPedido(pedido);
        return ResponseEntity.status(200).body(PedidoDtoOut.fromDocument(pedido));
    }

    // Endpoint que usará solo el cocinero para actualizar el estado de un pedido
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable String id,
            @RequestBody State estado
    ) {
        Optional<Pedido> pedidoOpt = pedidoService.obtenerPedido(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404).body("El pedido con el id: " + id + " no se ha encontrado");
        }

        Pedido pedido = pedidoOpt.get();
        pedido.setEstado(estado);

        pedido = pedidoService.crearPedido(pedido);
        return ResponseEntity.status(200).body(PedidoDtoOut.fromDocument(pedido));
    }

    @DeleteMapping("/delete/{mesa}")
    public ResponseEntity<?> borrarPedidos(
            @PathVariable String mesa
    ) {
        pedidoService.borrarPedidosPorMesa(mesa);
        return ResponseEntity.status(200).body("Pedidos de la mesa " + mesa + " borrados");
    }


}
