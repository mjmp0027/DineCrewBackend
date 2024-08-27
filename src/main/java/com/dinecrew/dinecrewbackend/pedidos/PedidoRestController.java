package com.dinecrew.dinecrewbackend.pedidos;

import com.dinecrew.dinecrewbackend.enums.Estado;
import com.dinecrew.dinecrewbackend.enums.State;
import com.dinecrew.dinecrewbackend.mesas.Mesa;
import com.dinecrew.dinecrewbackend.mesas.MesaService;
import com.dinecrew.dinecrewbackend.notificaciones.Notificacion;
import com.dinecrew.dinecrewbackend.notificaciones.NotificationService;
import com.dinecrew.dinecrewbackend.usuarios.User;
import com.dinecrew.dinecrewbackend.usuarios.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private MesaService mesaService;

    // Endpoint que obtiene un pedido para poder modificarlo
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


    // Endpoint para obtener los pedidos por usuario y role
    @GetMapping
    public ResponseEntity<?> obtenerPedidos(
            @RequestParam(required = false) String userId,
            @RequestParam String role
    ) {
        List<Pedido> pedidos;
        if ("CAMARERO".equals(role) && userId != null) {
            pedidos = pedidoService.obtenerPedidosPorUsuario(userId);
        } else {
            pedidos = pedidoService.obtenerPedidos();
        }
        return ResponseEntity.ok(PedidoDtoOut.fromDocuments(pedidos));
    }


    // Endpoint para obtener los pedidos por mesa y estado usando para calcular la cuenta
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
                .timestamp(LocalDateTime.now())
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

        // Si un pedido pasa a LISTO se enviará una notificación al usuario
        if (estado.equals(State.LISTO)) {
            Mesa mesa = mesaService.findByNumero(pedido.getMesa());
            if (mesa.getUserId() == null) {
                return ResponseEntity.status(400).body("La mesa no tiene camarero asignado");
            }
            notificationService.save(Notificacion.builder()
                    .mensaje("Pedido listo para la mesa " + pedido.getMesa())
                    .titulo("Pedido Listo")
                    .userId(mesa.getUserId())
                    .timestamp(LocalDateTime.now())
                    .leida(Estado.NOLEIDA)
                    .items(pedido.getItems())
                    .build());
            User user = userService.findById(mesa.getUserId());
            notificationService.sendOrderReadyNotification(pedido.getMesa(), user.getUsername(), pedido.getItems());
        }
        return ResponseEntity.status(200).body(PedidoDtoOut.fromDocument(pedido));
    }

    // Endpoint para terminar el servicio
    @DeleteMapping("/delete/{mesa}")
    public ResponseEntity<?> borrarPedidos(
            @PathVariable String mesa
    ) {
        pedidoService.borrarPedidosPorMesa(mesa);
        return ResponseEntity.status(200).body("Pedidos de la mesa " + mesa + " borrados");
    }


}
