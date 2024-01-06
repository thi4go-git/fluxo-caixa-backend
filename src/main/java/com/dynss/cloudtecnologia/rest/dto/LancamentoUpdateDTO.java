package com.dynss.cloudtecnologia.rest.dto;


import com.dynss.cloudtecnologia.anottation.UsuarioNaoLocalizado;
import com.dynss.cloudtecnologia.model.enums.Origem;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoUpdateDTO {

    @NotNull(message = "{campo.id.obrigatorio}")
    Long id;

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UsuarioNaoLocalizado
    private String username;

    @NotNull(message = "{campo.tipo.obrigatorio}")
    TipoLancamento tipo;

    @NotBlank(message = "{campo.descricao.obrigatorio}")
    String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{campo.data_lancamento.obrigatorio}")
    LocalDate dataLancamento;

    @NotNull(message = "{campo.valorParcela.obrigatorio}")
    BigDecimal valorParcela;

    @NotBlank(message = "{campo.natureza.obrigatorio}")
    String natureza;

    @NotNull(message = "{campo.situacao.obrigatorio}")
    Situacao situacao;

    @NotNull(message = "{campo.origem.obrigatorio}")
    Origem origem;
}
