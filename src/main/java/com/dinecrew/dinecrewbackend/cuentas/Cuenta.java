package com.dinecrew.dinecrewbackend.cuentas;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cuentas")
public class Cuenta {

    @Id
    private String id;
    private List<String> items;
    @CreatedDate
    private LocalDateTime timestamp;
    private double total;
}
