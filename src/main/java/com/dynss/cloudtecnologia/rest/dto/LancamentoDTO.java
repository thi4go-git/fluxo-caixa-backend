package com.dynss.cloudtecnologia.rest.dto;

import com.dynss.cloudtecnologia.anottation.UsuarioNaoLocalizado;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoDTO {

    private Long id;

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UsuarioNaoLocalizado
    private String username;

    @NotNull(message = "{campo.tipo.obrigatorio}")
    private TipoLancamento tipo;

    @NotBlank(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @NotNull(message = "{campo.data_referencia.obrigatorio}")
    private LocalDate data_referencia;

    @NotNull(message = "{campo.valor_total.obrigatorio}")
    private BigDecimal valor_total;

    @NotNull(message = "{campo.qtde_parcelas.obrigatorio}")
    private Integer qtde_parcelas;

    @NotNull(message = "{campo.id_natureza.obrigatorio}")
    private Long id_natureza;

}
