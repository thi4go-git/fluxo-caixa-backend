package com.dynss.cloudtecnologia.rest.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
public class LancamentoReflectionDTO {

    private String mes;
    private String mes_str;
    private BigDecimal saldo_entradas;
    private BigDecimal saldo_saidas;
}
