package com.dynss.cloudtecnologia.rest.dto;

import com.dynss.cloudtecnologia.anottation.UsuarioNaoLocalizado;
import com.dynss.cloudtecnologia.model.enums.Origem;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class LancamentoNewDTO {

    Long id;

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UsuarioNaoLocalizado
    private String username;

    @NotNull(message = "{campo.tipo.obrigatorio}")
    private TipoLancamento tipo;

    @NotNull(message = "{campo.origem.obrigatorio}")
    private Origem origem;

    @NotBlank(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{campo.data_referencia.obrigatorio}")
    private LocalDate dataReferencia;

    @NotNull(message = "{campo.valor_total.obrigatorio}")
    private BigDecimal valorTotal;

    @NotNull(message = "{campo.qtde_parcelas.obrigatorio}")
    private Integer qtdeParcelas;

    @NotNull(message = "{campo.id_natureza.obrigatorio}")
    private Long idNatureza;

}
