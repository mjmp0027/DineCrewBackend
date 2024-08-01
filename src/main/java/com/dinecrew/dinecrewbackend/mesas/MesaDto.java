package com.dinecrew.dinecrewbackend.mesas;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MesaDto {

    private String id;
    private String numero;
    private String userId;

    public static MesaDto fromDocument(Mesa mesa) {
        return MesaDto.builder()
                .id(mesa.getId())
                .numero(mesa.getNumero())
                .userId(mesa.getUserId())
                .build();
    }

    public static List<MesaDto> fromDocuments(List<Mesa> mesas) {
        return mesas.stream()
                .map(MesaDto::fromDocument)
                .collect(Collectors.toList());
    }
}
