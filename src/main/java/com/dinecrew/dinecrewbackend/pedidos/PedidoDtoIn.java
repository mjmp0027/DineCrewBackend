package com.dinecrew.dinecrewbackend.pedidos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoDtoIn {

    private String mesa;
    private List<String> items;
}
