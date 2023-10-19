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

    private Long id = null;
    private TipoLancamento tipo = null;
    private String descricao = "";
    private BigDecimal valorParcela = null;
    private Integer qtdeParcelas = null;
    private Integer nrParcela = null;
    private Integer idNatureza = null;
    private Situacao situacao = null;
    private String dataInicio;
    private String dataFim;

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UsuarioNaoLocalizado
    private String username = null;
}
