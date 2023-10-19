package com.dynss.cloudtecnologia.rest.dto;


import com.dynss.cloudtecnologia.model.entity.Natureza;
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
    private Long id;
    private TipoLancamento tipo;
    private String descricao;
    private LocalDate dataLancamento;
    private BigDecimal valorParcela;
    private Integer qtdeParcelas;
    private Integer nrParcela;
    private Natureza natureza;
    private Situacao situacao;
    private LocalDate dataCriacao;
    private LocalDate dataAlteracao;
    private String nomeAnexo;
}
