package com.dinecrew.dinecrewbackend.cuentas;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CuentaDto {

    private List<String> items;
    private double total;
}
