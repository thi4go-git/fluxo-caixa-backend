package com.dynss.cloudtecnologia.rest.dto;

import com.dynss.cloudtecnologia.anottation.UsuarioNaoLocalizado;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LancamentoFilterDTO {

    Long id = null;
    TipoLancamento tipo = null;
    String descricao = "";
    BigDecimal valorParcela = null;
    Integer qtdeParcelas = null;
    Integer nrParcela = null;
    Integer idNatureza = null;
    Situacao situacao = null;
    String dataInicio;
    String dataFim;

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UsuarioNaoLocalizado
    private String username = null;
}
