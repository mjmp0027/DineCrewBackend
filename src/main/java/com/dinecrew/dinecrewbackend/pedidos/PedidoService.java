package com.dinecrew.dinecrewbackend.pedidos;

import com.dinecrew.dinecrewbackend.enums.State;
import com.dinecrew.dinecrewbackend.mesas.Mesa;
import com.dinecrew.dinecrewbackend.mesas.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private MesaService mesaService;

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

    public List<Pedido> obtenerPedidosPorUsuario(String userId) {
        List<Mesa> mesas = mesaService.findAllByUserId(userId);

        List<String> mesaIds = mesas.stream().map(Mesa::getNumero).toList();
        return repository.findByMesaIn(mesaIds);

    }

    public void borrarPedidosPorMesa(String mesa) {
        repository.deleteAllByMesa(mesa);
    }
}
