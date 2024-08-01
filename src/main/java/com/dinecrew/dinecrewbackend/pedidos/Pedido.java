package com.dinecrew.dinecrewbackend.pedidos;

import com.dinecrew.dinecrewbackend.enums.State;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    private String id;
    private String mesa;
    private List<String> items;
    private State estado;

    @CreatedDate
    private Instant timestamp;
}
