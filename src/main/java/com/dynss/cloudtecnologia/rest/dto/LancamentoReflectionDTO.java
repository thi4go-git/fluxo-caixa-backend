package com.dynss.cloudtecnologia.rest.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class LancamentoReflectionDTO {
    String mes;
    String mesnumero;
    long entradas;
    long saidas;
}
