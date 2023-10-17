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
    private LocalDate data_lancamento;
    private BigDecimal valor_parcela;
    private Integer qtde_parcelas;
    private Integer nr_parcela;
    private Natureza natureza;
    private Situacao situacao;
    private LocalDate data_criacao;
    private LocalDate data_alteracao;
    private String nomeAnexo;
}
