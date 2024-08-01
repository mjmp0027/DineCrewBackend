package com.dinecrew.dinecrewbackend.pedidos;

import com.dinecrew.dinecrewbackend.enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public Optional<Pedido> obtenerPedido(String id) {
        return repository.findById(id);
    }

    public List<Pedido> obtenerPedidosPorMesaAndEstado(String id, State estado) {
        return repository.findAllByMesaAndEstado(id, estado);
    }

    public Pedido crearPedido(Pedido pedido) {
        return repository.save(pedido);
    }

    public List<Pedido> obtenerPedidos() {
        return repository.findAllByOrderByTimestampDesc();
    }

    public void borrarPedidosPorMesa(String mesa) {
        repository.deleteAllByMesa(mesa);
    }
}
