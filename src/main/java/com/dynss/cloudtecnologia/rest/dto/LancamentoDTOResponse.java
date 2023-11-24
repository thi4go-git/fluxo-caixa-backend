package com.dynss.cloudtecnologia.rest.dto;

import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoDTOResponse {
    Long id;
    TipoLancamento tipo;
    String descricao;
    LocalDate dataLancamento;
    BigDecimal valorParcela;
    Integer qtdeParcelas;
    Integer nrParcela;
    String natureza;
    Situacao situacao;
    LocalDate dataCriacao;
    LocalDate dataAlteracao;
    String nomeAnexo;
}
