package com.dinecrew.dinecrewbackend.pedidos;

import com.dinecrew.dinecrewbackend.enums.State;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PedidoRepository extends MongoRepository<Pedido, String> {

    List<Pedido> findAllByMesaAndEstado(String id, State estado);
    List<Pedido> findAllByOrderByTimestampDesc();

    void deleteAllByMesa(String mesa);
}
