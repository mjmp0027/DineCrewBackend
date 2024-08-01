package com.dinecrew.dinecrewbackend.mesas;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mesas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mesa {

    @Id
    private String id;
    private String numero;
    private String userId; // Id del camarero asignado (null si está libre)
}
