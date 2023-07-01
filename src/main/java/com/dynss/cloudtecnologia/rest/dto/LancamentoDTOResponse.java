package com.dynss.cloudtecnologia.rest.dto;


import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
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

    public LancamentoDTOResponse(Lancamento lancamento) {
        this.id = lancamento.getId();
        this.tipo = lancamento.getTipo();
        this.descricao = lancamento.getDescricao();
        this.data_lancamento = lancamento.getData_lancamento();
        this.valor_parcela = lancamento.getValor_parcela();
        this.qtde_parcelas = lancamento.getQtde_parcelas();
        this.nr_parcela = lancamento.getNr_parcela();
        this.natureza = lancamento.getNatureza();
        this.situacao = lancamento.getSituacao();
    }

}
