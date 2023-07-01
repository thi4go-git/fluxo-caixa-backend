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
    private BigDecimal valor_parcela = null;
    private Integer qtde_parcelas = null;
    private Integer nr_parcela = null;
    private Integer id_natureza = null;
    private Situacao situacao = null;
    private String data_inicio;
    private String data_fim;

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UsuarioNaoLocalizado
    private String username = null;
}
