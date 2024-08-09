package com.dinecrew.dinecrewbackend.pedidos;


import com.dinecrew.dinecrewbackend.enums.State;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoDtoOut {

    private String id;
    private String mesa;
    private List<String> items;
    private State estado;
    private LocalDateTime timestamp;

    public static PedidoDtoOut fromDocument(Pedido pedido) {
        return new PedidoDtoOut(pedido.getId(), pedido.getMesa(), pedido.getItems(), pedido.getEstado(), pedido.getTimestamp());
    }

    public static List<PedidoDtoOut> fromDocuments(List<Pedido> pedidos) {
        return pedidos.stream().map(PedidoDtoOut::fromDocument).toList();
    }
}
